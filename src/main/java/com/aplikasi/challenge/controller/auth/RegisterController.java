package com.aplikasi.challenge.controller.auth;

import com.aplikasi.challenge.config.Config;
import com.aplikasi.challenge.entity.Merchant;
import com.aplikasi.challenge.entity.User;
import com.aplikasi.challenge.repository.oauth.UserRepository;
import com.aplikasi.challenge.dto.RegisterDTO;
import com.aplikasi.challenge.service.MerchantService;
import com.aplikasi.challenge.service.email.EmailSender;
import com.aplikasi.challenge.service.UserService;
import com.aplikasi.challenge.utils.EmailTemplate;
import com.aplikasi.challenge.utils.SimpleStringUtils;
import com.aplikasi.challenge.utils.TemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/v1/user-register/")
@Tag(name = "Register", description = "User Register APIs")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    public EmailSender emailSender;
    @Autowired
    public EmailTemplate emailTemplate;
    @Autowired
    public MerchantService merchantService;

    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
    private int expiredToken;


    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Autowired
    public TemplateResponse templateResponse;
    @PostMapping("/register-user")
    @Operation(summary = "Register User", description = "Register User")
    public ResponseEntity<Map> saveRegisterManual(@Valid @RequestBody RegisterDTO objModel) throws RuntimeException {

        User user = userRepository.checkExistingEmail(objModel.getUsername());
        if (null != user) {
            return new ResponseEntity<Map>(templateResponse.error("Username sudah ada"), HttpStatus.OK);

        }
         Map map = serviceReq.registerManual(objModel);

        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping("/register-merchant/{id}")
    public ResponseEntity<Map> saveRegisterManual(@Valid @RequestBody Merchant objModel, @PathVariable("id") Long id) throws RuntimeException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<Map>(templateResponse.error("User not found"), HttpStatus.OK);
        } else if (user.get().getMerchant() != null) return new ResponseEntity<Map>(templateResponse.error("Account merchant already exist"), HttpStatus.OK);
        merchantService.save(objModel);
        Map map = serviceReq.registerMerchant(user.get());

        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping("/register-google")
    public ResponseEntity<Map> saveRegisterByGoogle(@Valid @RequestBody RegisterDTO objModel) throws RuntimeException {
        User user = userRepository.checkExistingEmail(objModel.getUsername());
        if (null != user) {
            return new ResponseEntity<Map>(templateResponse.error("Username sudah ada"), HttpStatus.OK);

        }
        serviceReq.registerByGoogle(objModel);
        //gunanya send email
        Map mapRegister =  sendEmailegister(objModel);
        return new ResponseEntity<Map>(mapRegister, HttpStatus.OK);

    }



    // Step 2: sendp OTP berupa URL: guna updeta enable agar bisa login:
    @PostMapping("/send-otp")//send OTP
    public Map sendEmailegister(
            @RequestBody RegisterDTO user) {
        String message = "Thanks, please check your email for activation.";

        if (user.getUsername() == null) return templateResponse.error("No email provided");
        User found = userRepository.findOneByUsername(user.getUsername());
        if (found == null) return templateResponse.error("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getRegisterTemplate();
        if (found.getOtp() == null) {
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

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replace("\\{\\{USERNAME}}", (found.getFullname() == null ? found.getUsername() : found.getFullname()));
            template = template.replace("\\{\\{VERIFY_TOKEN}}",  otp);
            userRepository.save(found);
        } else {
            template = template.replace("\\{\\{USERNAME}}", (found.getFullname() == null ? found.getUsername() : found.getFullname()));
            template = template.replace("\\{\\{VERIFY_TOKEN}}",  found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Register", template);
        return templateResponse.success(message);
    }

    @GetMapping("/register-confirm-otp/{token}")
    public ResponseEntity<Map> saveRegisterManual(@PathVariable(value = "token") String tokenOtp) throws RuntimeException {



        User user = userRepository.findOneByOTP(tokenOtp);
        if (null == user) {
            return new ResponseEntity<Map>(templateResponse.error("OTP tidak ditemukan"), HttpStatus.OK);
        }
//validasi jika sebelumnya sudah melakukan aktifasi

        if(user.isEnabled()){
            return new ResponseEntity<Map>(templateResponse.error("Akun Anda sudah aktif, Silahkan melakukan login"), HttpStatus.OK);
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if(Long.parseLong(today) > Long.parseLong(dateToken)){
            return new ResponseEntity<Map>(templateResponse.error("Your token is expired. Please Get token again."), HttpStatus.OK);
        }
        //update user
        user.setEnabled(true);
        userRepository.save(user);

        return new ResponseEntity<Map>(templateResponse.success("Sukses, Silahkan Melakukan Login"), HttpStatus.OK);
    }


}
