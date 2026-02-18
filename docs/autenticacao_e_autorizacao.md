# Documentação de Autenticação e Autorização no Projeto Cinema

## 1. Estrutura de Usuário e Perfis

### Classe `Usuario`
- Representa o usuário no sistema.
- Campos principais:
  - `id` (UUID) → identificador único.
  - `nome`, `email`, `senha`.
  - `tipoUsuario` (enum `TipoUsuario`) → define o perfil do usuário.
  - `saldo` → saldo disponível.
  - `ingressos` → ingressos comprados.

### Enum `TipoUsuario`
- Define os perfis de acesso:
  - `ADMINISTRADOR` → acesso total, inclusive endpoints administrativos.
  - `CLIENTE` → acesso a funcionalidades de compra e saldo.
  - `VISITANTE` → acesso restrito, apenas leitura ou navegação básica.

---

## 2. Configuração de Segurança (`SecurityConfig`)

### Principais pontos:
- **CSRF desabilitado**: útil para APIs REST.
- **CORS habilitado**: permite que o frontend (Vue.js em `http://localhost:5173`) acesse a API.
- **Autorização baseada em roles**:
  ```java
  .authorizeHttpRequests(auth -> auth
      .requestMatchers("/api/usuarios/cadastro", "/api/usuarios/login").permitAll()
      .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
      .requestMatchers("/api/cliente/**").hasRole("CLIENTE")
      .requestMatchers("/api/visitante/**").hasRole("VISITANTE")
      .anyRequest().authenticated()
  )
  ```
- **Sessão**:
  - `SessionCreationPolicy.IF_REQUIRED` → cria sessão apenas quando necessário.
  - `maximumSessions(1)` → cada usuário só pode ter uma sessão ativa.
  - `invalidSessionUrl("/api/usuarios/session-expired")` → endpoint para sessão expirada.

---

## 3. Configuração de CORS (`CorsConfig`)

- Permite que o frontend em `http://localhost:5173` faça requisições.
- Métodos permitidos: `GET, POST, PUT, DELETE, OPTIONS`.
- `allowCredentials(true)` → garante envio de cookies (sessão).
- `same-site=None` + `secure=true` → cookies de sessão só são enviados em HTTPS e podem ser usados em requisições cross-site.

---

## 4. Controle de Permissões por Usuário

### Integração com Spring Security
- O `Usuario` precisa ser convertido em `UserDetails` para que o Spring reconheça suas permissões.
- Exemplo de implementação:
  ```java
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().name()));
  }
  ```
- Assim, um `ADMINISTRADOR` terá a autoridade `ROLE_ADMINISTRADOR`.

### Uso em Controllers
- Proteção de endpoints com anotações:
  ```java
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/dashboard")
  public String dashboard() {
      return "Área restrita do administrador";
  }
  ```

---

## 5. Comparação: Sessões vs JWT

| Aspecto | Sessões (Stateful) | JWT (Stateless) |
|---------|--------------------|-----------------|
| **Armazenamento** | Servidor mantém estado da sessão (cookie `JSESSIONID`). | Token JWT é armazenado no cliente (localStorage ou cookie). |
| **Escalabilidade** | Difícil em ambientes distribuídos (precisa replicar sessões). | Fácil escalar, pois não há estado no servidor. |
| **Expiração** | Controlada pelo servidor (`server.servlet.session.timeout=30m`). | Embutida no token (`exp` claim). |
| **Logout** | Invalidação da sessão no servidor. | Precisa blacklist ou expiração curta do token. |
| **Segurança** | Cookies seguros (`SameSite=None`, `Secure=true`). | JWT assinado digitalmente, mas precisa proteção contra roubo. |
| **Controle de concorrência** | Fácil limitar sessões por usuário (`maximumSessions(1)`). | Mais difícil, pois tokens são independentes. |
| **Uso típico** | Aplicações web tradicionais com login persistente. | APIs REST modernas, microserviços, mobile apps. |

---

## 6. Evolução do Projeto

### Com Sessões (atual)
- Usuário faz login → servidor cria sessão → cookie `JSESSIONID` é enviado.
- Sessão expira em 30 minutos sem atividade.
- Apenas 1 sessão por usuário.
- Roles controlam acesso a endpoints.
- **Mais simples de implementar e manter**, pois o Spring já gerencia automaticamente criação, expiração e invalidação de sessões.

### Com JWT (possível evolução)
- Usuário faz login → servidor gera JWT com claims (`email`, `tipoUsuario`, `exp`).
- JWT é enviado ao cliente e usado em cada requisição (`Authorization: Bearer <token>`).
- Servidor não mantém estado → mais escalável.
- Roles são verificadas diretamente no token.
- **Mais flexível**, mas exige configuração extra de geração, assinatura e validação de tokens.

---

## 7. Conclusão

- **Modelo atual (sessões)**:  
  - É suficiente para o projeto, já que o consumo será feito por um frontend Vue.js rodando em navegador.  
  - Reduz a complexidade do desenvolvimento, pois o Spring cuida de toda a gestão de sessão e cookies.  
  - Permite controle de concorrência (apenas uma sessão por usuário) e expiração automática.  
  - Continua sendo uma prática válida e bem estruturada em sistemas web, mesmo que JWT seja tendência em APIs modernas.  

- **Modelo JWT**:  
  - Ideal para cenários de escalabilidade distribuída, integração com mobile apps ou APIs públicas.  
  - Mais flexível, mas adiciona complexidade que não é necessária no estágio atual do projeto.  

Portanto, **sessões são a escolha mais simples e adequada para este projeto**, garantindo segurança, controle de permissões e menor esforço de implementação. JWT pode ser considerado como evolução futura, caso a API precise atender múltiplos clientes ou escalar em ambientes distribuídos.  
