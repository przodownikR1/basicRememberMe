package pl.java.scalatech.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@Import(PopulatorConfig.class)
@EnableWebSecurity(debug = false)
@ComponentScan(basePackages = "pl.java.scalatech.security")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${logout.url:welcome}")
    private String logoutUrl;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("classpath:persistent_logins.sql")
    private Resource schemaScript;


    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        try {
            String script = Resources.toString(schemaScript.getURL(), Charsets.UTF_8);
            jdbcTemplate.execute(script);
        } catch (final IOException e) {
            throw new RuntimeException("Unable to convert resource " + schemaScript.getFilename(), e);
        }

    }

    @Bean
    public static SessionRegistry getSessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        // db.setCreateTableOnStartup(true);
        db.setDataSource(dataSource);
        return db;
    }
    @Bean
    public PersistentTokenRepository tokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        log.info("LOGIN FORM  .......");
        // @formatter:off
            web.ignoring().antMatchers("/assets/**").antMatchers("/resources/**").antMatchers("/favicon.ico").antMatchers("/webjars/**");
            // @formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // @formatter:off
            http.csrf().disable().headers().disable().authorizeRequests()
            .antMatchers("/login","/loginUser", "/logout",  "/principal", "/health", "/console","/logAuth/**")
                    .permitAll()
                    .antMatchers("secContext").hasAnyRole("USER")
                    .antMatchers("/user/sessions/").hasAnyRole("USER","ADMIN")
                    .antMatchers("/userTest").hasRole("USER")
                    .antMatchers("/userTest/*").hasRole("USER")
                    .antMatchers("/userTest/principal").hasRole("USER")
                    .antMatchers("/guestTest").anonymous()
                    .antMatchers("/adminTest").hasRole("ADMIN")
                    .antMatchers("/adminTest2").hasRole("ADMIN")
                    .antMatchers("/adminTest3").hasRole("ADMIN")
                    .antMatchers("/user/** ").hasAnyRole("USER","ADMIN")
                    .antMatchers("/simple/**").hasAnyRole("USER")
                    .antMatchers("/ip").hasIpAddress("127.0.0.1")  //antMatchers("/ipsecure/**").access("hasIpAddress('127.0.0.1')")
                    .antMatchers("/userOne").access("hasRole('ROLE_ADMIN')")
                    .and().authorizeRequests().anyRequest().authenticated()

                    .and().formLogin()
                    .loginPage("/login").permitAll().defaultSuccessUrl("/welcome").failureUrl("/login?error").permitAll()
                    .and()
                    .logout().permitAll(true).logoutSuccessUrl("/login/?logout").deleteCookies("JSESSIONID","rememberMeCookie").invalidateHttpSession(true)
                    .and()
                    .rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(2419200)
                   // .rememberMe().tokenValiditySeconds(10000).tokenRepository(persistentTokenRepository()).useSecureCookie(true)
                    .and().anonymous()
                    .principal("guest")
                    .authorities("ROLE_GUEST").and();
                   // .rememberMe().tokenRepository(tokenRepository()).tokenValiditySeconds(2419200).key("userKey").useSecureCookie(true);
            // @formatter:on
    }



    @Autowired
    public void configureGlobal(UserDetailsService userDetailsService, AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        log.info("password Encoding {}", passwordEncoder);
        auth.userDetailsService(userDetailsService);

    }

}

/*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        log.info("password Encoding {}", passwordEncoder);
        // @formatter:off
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder)
        .withUser("przodownik").password("$2a$10$vGdVdtvx9jGTVs1uuywXyOiYovelvWWUFBIMbS5pSNuWmcCZlx.86").roles("USER").and()
        .withUser("aga").password("$2a$10$vGdVdtvx9jGTVs1uuywXyOiYovelvWWUFBIMbS5pSNuWmcCZlx.86").roles("BUSINESS").and()
        .withUser("vava").password("$2a$10$vGdVdtvx9jGTVs1uuywXyOiYovelvWWUFBIMbS5pSNuWmcCZlx.86").roles("USER").and()
        .withUser("bak").password("$2a$10$vGdVdtvx9jGTVs1uuywXyOiYovelvWWUFBIMbS5pSNuWmcCZlx.86").roles("USER", "ADMIN");
        // @formatter:on
    }
*/
