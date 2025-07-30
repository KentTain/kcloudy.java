package kc.service.account;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import kc.framework.security.MD5Provider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@lombok.extern.slf4j.Slf4j
public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            String pwd = MD5Provider.Hash((String) rawPassword, false);
            //log.info("MD5PasswordEncoder 加密后的密码: " + rawPassword);
            //log.info("MD5PasswordEncoder 传入后的密码: " + encodedPassword);
            return encodedPassword.equals(pwd);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return MD5Provider.Hash((String) rawPassword, false);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
