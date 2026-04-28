/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.frontendservice.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

public class JWTUtil {

    private static final String SECRET = "my-secret-key";

    public static String generateToken(String username) {

        return JWT.create()
                .withSubject(username)
                .withIssuer("InventorySystem")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(Algorithm.HMAC256(SECRET));
    }
}
