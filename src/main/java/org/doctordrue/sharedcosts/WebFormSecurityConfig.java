package org.doctordrue.sharedcosts;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @author Andrey_Barantsev
 * 3/28/2022
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebFormSecurityConfig extends WebSecurityConfigurerAdapter {

   @Autowired
   private PasswordEncoder encoder;
   @Autowired
   private PersonService personService;
   @Value("${app.admin.username}")
   private String adminUserName;
   @Value("${app.admin.password}")
   private String adminPassword;

   @Bean
   public PersistentTokenRepository persistentTokenRepository() {
      return new InMemoryTokenRepositoryImpl();
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
              .headers().frameOptions().disable().and()
              .authorizeRequests()
              .antMatchers("/h2-console/**").permitAll()
              .and()
              .authorizeRequests()
              .antMatchers("/api/**").permitAll()
              .antMatchers("/", "/styles/**").permitAll()
              .antMatchers("/register").not().fullyAuthenticated()
              .antMatchers("/persons/**", "/currency/**").hasRole(RoleType.ADMIN.name())
              .antMatchers("/self").hasAnyRole(RoleType.ADMIN.name(), RoleType.USER.name())
              .antMatchers("/**").fullyAuthenticated()
              .anyRequest().authenticated()
              .and()
              .formLogin()
              .loginPage("/login")
              .usernameParameter("email")
              .passwordParameter("password")
              .defaultSuccessUrl("/", true)
              .permitAll()
              .and()
              .logout()
              .permitAll()
              .logoutSuccessUrl("/login")
              .and().rememberMe()
              .tokenRepository(persistentTokenRepository());
   }

   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      adminUserName = "admin@email.com";
      adminPassword = "admin";
      auth
              .userDetailsService(personService)
              .passwordEncoder(encoder)
              .and()
              .inMemoryAuthentication()
              .withUser(adminUserName)
              .password(encoder.encode(adminPassword))
              .roles("ADMIN");
   }
}
