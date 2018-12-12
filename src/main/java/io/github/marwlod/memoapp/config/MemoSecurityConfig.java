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

    @Value("${memo.queries.users}")
    private String usersQuery;

    @Value("${memo.queries.roles}")
    private String rolesQuery;

    @Autowired
    public MemoSecurityConfig(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.dataSource = dataSource;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/register", "/confirmRegistration")
                    .permitAll()
                .antMatchers("/uniMemo/**")
                    .hasAuthority("USER")
                .anyRequest()
                    .authenticated().and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .defaultSuccessUrl("/uniMemo/list")
                .usernameParameter("email")
                    .passwordParameter("password").and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

}
