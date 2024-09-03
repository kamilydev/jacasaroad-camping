package com.camping.jacasaroad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebSecurityConfig  implements WebMvcConfigurer {

    private final CustomUserDetailsService customUserDetailsService;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Desabilita CSRF
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/usuario/updateUser",
                                "/usuario/viewUpdateUser",
                                "/avaliacao/",
                                "/avaliacao/anfitriao/",
                                "/avaliacao/espaco/",
                                "/espaco/",
                                "/espaco/anfitriao/",
                                "/espaco/atualizar/",
                                "/espaco/criarEspaco",
                                "/espaco/deletar/",
                                "/espaco/filtrar",
                                "/espaco/listarEspacos",
                                "/espaco/viewAtualizar/",
                                "/favoritos/",
                                "/favoritos/adicionarFavorito",
                                "/favoritos/excluirFavorito",
                                "/favoritos/listarFavoritos",
                                "/reserva/",
                                "/reserva/criarReserva",
                                "/reserva/anfitriao/cancelar/",
                                "/reserva/atualizar/",
                                "/reserva/cancelar/"
                        ).authenticated()  // Requer autenticação para /updateUser
                        .anyRequest().permitAll()  // Permite todos os outros endpoints
                )
                .httpBasic(Customizer.withDefaults());  // Habilita Basic Auth com a nova API

        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)  // Desabilita CSRF
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/updateUser").authenticated()  // Requer autenticação para /updateUser
//                        .anyRequest().permitAll()  // Permite todos os outros endpoints
//                )
//                .formLogin((form) -> form
//                        .loginPage("/usuario/login")  // Página personalizada de login
//                        .defaultSuccessUrl("/home", true)  // Redireciona para /home após login bem-sucedido
//                        .failureUrl("/usuario/login?error=true")  // Redireciona para /login com erro em caso de falha
//                        .permitAll()
//                )
//                .logout((logout) -> logout
//                        .logoutUrl("/logout")  // URL de logout
//                        .logoutSuccessUrl("/usuario/login?logout=true")  // Redireciona para login após logout
//                        .permitAll()
//                );
//
//        return http.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/usuario/home").setViewName("homeLogin");
        registry.addViewController("/usuario/home").setViewName("homeLogout");
        registry.addViewController("/login").setViewName("userLogin");
        registry.addViewController("/usuario/logout").setViewName("homeLogout");
        registry.addViewController("/usuario/updateUser").setViewName("userAtualizarUsuario");
        registry.addViewController("/usuario/viewUpdateUser").setViewName("userAtualizarUsuario");
        registry.addViewController("/usuario/newUser").setViewName("userCadastroUsuario");
        registry.addViewController("/espaco/criarEspaco").setViewName("adsCriarAnuncio");
        registry.addViewController("/espaco/viewAtualizar/{id}").setViewName("adsAtualizarAnuncio");
        registry.addViewController("/espaco/Atualizar/{id}").setViewName("adsAtualizarAnuncio");
        registry.addViewController("/espaco/listarEspacos").setViewName("explorerVisualizarTodosAnuncios");
        registry.addViewController("/espaco/anfitriao/{nomeDeUsuario}").setViewName("explorerVisualizarTodosAnuncios");
        registry.addViewController("/espaco/filtrar").setViewName("explorerVisualizarTodosAnuncios");
        registry.addViewController("/avaliacao/espaco/{espacoId}").setViewName("explorerVisualizarAnuncio");
        registry.addViewController("/avaliacao/anfitriao/{espacoId}").setViewName("adsRelatorios");
        registry.addViewController("/favoritos/listarFavoritos").setViewName("configMeusFavoritos");
        registry.addViewController("/reserva/criarReserva").setViewName("explorerVisualizarAnuncio");
        registry.addViewController("/reserva/{nomeDeUsuario}").setViewName("configMinhasReservas");
        registry.addViewController("/reserva/anfitriao/{nomeDeUsuarioAnfitriao}").setViewName("adsRelatorios");


    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/js/**", "/images/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/js/", "classpath:/static/images/");
    }

}