# Login page

### 1. `repository.UserRepository`에서 Optional을 사용하는 이유

- Optional은 값이 없으면 null을 반환하는 역할을 한다. 하지만 여기서 username과 email은 사용자가 사이트를 사용하려면 무조건 있어야 하는 값이여야한다. 그래도 왜 Optional을 사용하는지 궁금했다.

- 이유: 로그인시 잘못된 ID입력, 회원가입 시 중복체크, 비밀번호 재설정(해당 이메일이 없으면 오류반환) 등 이러한 오류를 처리해야하기 때문이다.

    핵심은 DB에는 반드시 존재하는건 맞지만 사용자가 잘못된 username,email 입력 등 이러한 오류를 처리하기 위해서는 `Optional` 이 필요하다.

### 2. `UserRepository`에서 interface 사용하는 이유

- 핵심 : interface를 사용하면 내가 원하는 동작을 JPA 쿼리를 자동 생성해서 실행해준다.

- SpringData JPA는 `Optional<User> findByUsername(String username);`(interface)를 보고 자동으로 구현체를 생성

- 만약에 interface를 안 쓰고 class로 구현할 경우 : 
  ```
  @Repository
  public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public Optional<User> findByUsername(String username) {
        String jpql = "SELECT u FROM User u WHERE u.username = :username";
        return em.createQuery(jpql, User.class)
                 .setParameter("username", username)
                 .getResultStream()
                 .findFirst();
    }
  ```
- 이런식으로 매 Method 마다 Query + Parameter + 결과 처리를 작성해야하는데 이 과정에서 유지보수, 오타 등 실수가 발생한다.

### 3. interface & class

- interface : 원하는 기능을 정의만 하고, 구현은 하지 않음

  그래서 다양한 class들이 같은 기능을 가지게 하려면 `class implements interface` 처럼 구현한다

- class : 실제로 그 기능이 어떻게 작동할지 코드를 채워넣는 역할

### 4. DTO (Data Transfer Object)

- Client(Frontend) <-> Server 간에 데이터를 주고받을 때 사용하는 전용 객체

### 5. 개발의 흐름

- Client -> (JSON Data) -> DTO -> Controller -> Service -> Repository -> DB