package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.config.Config;
import com.aplikasi.challenge.dto.user.password.ChangePasswordDTO;
import com.aplikasi.challenge.dto.user.password.EmailValidationDTO;
import com.aplikasi.challenge.entity.oauth.Role;
import com.aplikasi.challenge.entity.User;
import com.aplikasi.challenge.repository.oauth.RoleRepository;
import com.aplikasi.challenge.repository.oauth.UserRepository;
import com.aplikasi.challenge.dto.LoginDTO;
import com.aplikasi.challenge.dto.RegisterDTO;
import com.aplikasi.challenge.service.UserService;
import com.aplikasi.challenge.service.email.EmailSender;
import com.aplikasi.challenge.utils.EmailTemplate;
import com.aplikasi.challenge.utils.PasswordValidatorUtil;
import com.aplikasi.challenge.utils.SimpleStringUtils;
import com.aplikasi.challenge.utils.TemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    Config config = new Config();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${BASEURL}")
    private String baseUrl;
    @Autowired
    RoleRepository repoRole;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    UserRepository repoUser;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    public TemplateResponse templateResponse;
    @Autowired
    public PasswordValidatorUtil passwordValidatorUtil = new PasswordValidatorUtil();
    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
    private int expiredToken;
    @Autowired
    public EmailTemplate emailTemplate;
    @Autowired
    public EmailSender emailSender;



    @Override
    public Map login(LoginDTO loginModel) {
        /**
         * bussines logic for login here
         * **/
        try {
            Map<String, Object> map = new HashMap<>();

            User checkUser = userRepository.findOneByUsername(loginModel.getUsername());

            if ((checkUser != null) && (encoder.matches(loginModel.getPassword(), checkUser.getPassword()))) {
                if (!checkUser.isEnabled()) {
                    return templateResponse.error("User is not enable, check your email");
                }
            }
            if (checkUser == null) {
                return templateResponse.error("user not found");
            }
            if (!(encoder.matches(loginModel.getPassword(), checkUser.getPassword()))) {
                return templateResponse.error("wrong password");
            }
            String url = baseUrl + "/oauth/token?username=" + loginModel.getUsername() +
                    "&password=" + loginModel.getPassword() +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                    ParameterizedTypeReference<Map>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK) {
                User user = userRepository.findOneByUsername(loginModel.getUsername());
                List<String> roles = new ArrayList<>();

                for (Role role : user.getRoles()) {
                    roles.add(role.getName());
                }
                //save token
//                checkUser.setAccessToken(response.getBody().get("access_token").toString());
//                checkUser.setRefreshToken(response.getBody().get("refresh_token").toString());
//                userRepository.save(checkUser);

                map.put("access_token", response.getBody().get("access_token"));
                map.put("token_type", response.getBody().get("token_type"));
                map.put("refresh_token", response.getBody().get("refresh_token"));
                map.put("expires_in", response.getBody().get("expires_in"));
                map.put("scope", response.getBody().get("scope"));
                map.put("jti", response.getBody().get("jti"));
                map.put("message","Success");
                map.put("code",200);

                return map;
            } else {
                return templateResponse.error("user not found");
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return templateResponse.error("invalid login : " + e.getMessage());
            }
            return templateResponse.error(e);
        } catch (Exception e) {
            e.printStackTrace();

            return templateResponse.error(e);
        }
    }

    @Override
    public Map registerManual (RegisterDTO objModel) {
        try {
            String[] roleNames = {"ROLE_USER, ROLE_USER_O, ROLE_USER_OD"}; // admin
            User user = new User();
            user.setUsername(objModel.getUsername().toLowerCase());
            user.setFullname(objModel.getFullname());

//            //step 1 :
//            user.setEnabled(false); // matikan user
            if (objModel.getPassword().isEmpty()) return templateResponse.error("Password is required");
            if (!passwordValidatorUtil.validatePassword(objModel.getPassword())) {
                return templateResponse.error(passwordValidatorUtil.getMessage());
            }
            String password = encoder.encode(objModel.getPassword().replaceAll("\\s+", ""));
            List<Role> r = repoRole.findByNameIn(roleNames);
            user.setRoles(r);
            user.setPassword(password);
            User obj = repoUser.save(user);

            return templateResponse.success(obj);

        } catch (Exception e) {
            logger.error("Eror registerManual=", e);
            return templateResponse.error("eror:"+e);
        }

    }
    @Override
    public Map registerMerchant (User request) {
        try {
            Optional<User> checkDataDBUser = userRepository.findById(request.getId());
            String[] roleNames = {"ROLE_MERCHANT", "ROLE_MERCHANT_P"}; // admin
            List<Role> r = repoRole.findByNameIn(roleNames);
            List<Role> existingRole = checkDataDBUser.get().getRoles();
            existingRole.addAll(r);
            checkDataDBUser.get().setRoles(existingRole);
            User obj = repoUser.save(checkDataDBUser.get());

            return templateResponse.success(obj);

        } catch (Exception e) {
            logger.error("Eror registerManual=", e);
            return templateResponse.error("eror:"+e);
        }

    }
    @Override
    public Map registerByGoogle(RegisterDTO objModel) {
        try {
            String[] roleNames = {"ROLE_USER, ROLE_USER_O, ROLE_USER_OD"};
            User user = new User();
            user.setUsername(objModel.getUsername().toLowerCase());
            user.setFullname(objModel.getFullname());
            //step 1 :
            user.setEnabled(false); // matikan user
            String password = encoder.encode(objModel.getPassword().replaceAll("\\s+", ""));
            List<Role> r = repoRole.findByNameIn(roleNames);
            user.setRoles(r);
            user.setPassword(password);
            User obj = repoUser.save(user);
            return templateResponse.success(obj);

        } catch (Exception e) {
            logger.error("Eror registerManual=", e);
            return templateResponse.error("eror:"+e);
        }
    }

    @Override
    public Map<Object, Object> update(User request) {
        try {
            log.info("Update User");
            if (request.getId() == null) return templateResponse.error("Id is required");
            Optional<User> checkDataDBUser = userRepository.findById(request.getId());
            if (!checkDataDBUser.isPresent()) return templateResponse.error("Id is not Registered");
            if (!request.getFullname().isEmpty()) {
                checkDataDBUser.get().setFullname(request.getFullname());
            }

            log.info("Update User Success");
            return templateResponse.success(userRepository.save(checkDataDBUser.get()));
        } catch (Exception e) {
            log.error("Update User Error: " + e.getMessage());
            return templateResponse.error("Update User: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> forgotPasswordRequest(EmailValidationDTO request) {
        log.info("Change Password");
        User checkUser = userRepository.findOneByUsername(request.getEmail());
        if (checkUser == null) return templateResponse.error("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getResetPassword();
        if (checkUser.getOtp() == null) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            checkUser.setOtp(otp);
            checkUser.setOtpExpiredDate(expirationDate);
            template = template.replace("{{PASS_TOKEN}}", otp);

            userRepository.save(checkUser);
        } else {
            template = template.replace("{{PASS_TOKEN}}", checkUser.getOtp());
        }
        template = template.replace("{{USERNAME}}", checkUser.getFullname());
        emailSender.sendAsync(checkUser.getUsername(), "Forget Password OTP Request", template);
        log.info("Forgot Password OTP Request Success");
        return templateResponse.success("Please check email for reset password");
    }

    @Override
    public Map<Object, Object> checkOtpValidity(String request) {
        log.info("Check OTP Validity");
        if (request == null) return templateResponse.error("Token " + config.isRequired);
        User user = userRepository.findOneByOTP(request);
        if (user == null) {
            return templateResponse.error("Token not valid");
        }

        log.info("Check OTP Validity Success");
        return templateResponse.success("OTP is valid");
    }

    @Override
    public Map<Object, Object> changePassword(ChangePasswordDTO request) {
        if (request.getOtp() == null) return templateResponse.error("Token " + config.isRequired);
        if (request.getNewPassword() == null) return templateResponse.error("New Password " + config.isRequired);
        User user = userRepository.findOneByOTP(request.getOtp());
        String success;
        if (user == null) return templateResponse.error("Token not valid");

        if (!passwordValidatorUtil.validatePassword(request.getNewPassword())) {
            return templateResponse.error(passwordValidatorUtil.getMessage());
        }
        user.setPassword(encoder.encode(request.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        try {
            userRepository.save(user);
            success = "success";
        } catch (Exception e) {
            return templateResponse.error("Gagal simpan user");
        }
        return templateResponse.success(success);
    }

    @Override
    public Map<Object, Object> delete(User request) {
        try {
            log.info("Delete User");
            if (request.getId() == null) return templateResponse.error("Id is required");
            Optional<User> checkDataDBUser = userRepository.findById(request.getId());
            if (!checkDataDBUser.isPresent()) return templateResponse.error("User not Found");

            log.info("User Deleted");
            checkDataDBUser.get().setDeletedDate(new Date());
            return templateResponse.success(userRepository.save(checkDataDBUser.get()));
        } catch (Exception e) {
            log.error("Delete User Error: " + e.getMessage());
            return templateResponse.error("Delete User : " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getById(Long id) {
        try {
            log.info("Get User");
            if (id == null) return templateResponse.error("Id is required");
            Optional<User> checkDataDBUser = userRepository.findById(id);
            if (!checkDataDBUser.isPresent()) return templateResponse.error("User not Found");

            log.info("User Found");
            return templateResponse.success(checkDataDBUser.get());
        } catch (Exception e) {
            log.error("Get User Error: " + e.getMessage());
            return templateResponse.error("Get User: " + e.getMessage());
        }
    }

}

