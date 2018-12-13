package io.github.marwlod.memoapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class MemoSecurityConfig extends WebSecurityConfigurerAdapter {

    private DataSource dataSource;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${query.users}")
    private String usersQuery;

    @Value("${query.roles}")
    private String rolesQuery;

    @Autowired
    public MemoSecurityConfig(DataSource dataSource,
                              BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.dataSource = dataSource;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // database authentication, telling how to get users, roles and which password encoder
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // all requests must be authenticated except for these below
                .antMatchers("/login", "/register", "/confirmRegistration")
                    .permitAll()
                .antMatchers("/uniMemo/**")
                    .hasAuthority("USER")
                .anyRequest()
                    .authenticated().and()

                // use custom login page and related
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .defaultSuccessUrl("/uniMemo/list")

                // authentication method
                .usernameParameter("email")
                    .passwordParameter("password").and()

                // logout handled by spring
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .deleteCookies("JSESSIONID").and()

                // remember me cookie
                .rememberMe()
                    .key("secretKey")
                    .tokenValiditySeconds(60*60*24*7);
    }

}
