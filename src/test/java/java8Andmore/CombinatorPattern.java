package java8Andmore;

import java.util.function.Function;

/**
 * Combinator Pattern
 */
public class CombinatorPattern {

    public interface UserRegistryValidator extends Function<User, Validator> {

        static UserRegistryValidator isEmailValid(){
            return user -> user.email.contains("@") ? Validator.SUCCESS : Validator.BAD_EMAIL;
        }

        static UserRegistryValidator isValidName(){
            return u -> null != u.name ? Validator.SUCCESS : Validator.BAD_NAME;
        }

//        static UserRegistryValidator validBoth(String data){
//            return s -> s.email
//        }

        /**
         * Combinator logic impl
         * @param other
         * @return
         */
        default UserRegistryValidator and(UserRegistryValidator other){
            return user -> {
                Validator result = this.apply(user);
                if(result == Validator.SUCCESS){
                    return other.apply(user);
                }
                return result;
            };
        }

    }

    public static class User{ //Static class is allowed on nested classes only. it cannot be used on Outer class
        String name;
        String email;

        private User(String name, String email){
            this.name = name;
            this.email = email;
        }

        public static User of(String name, String email){
            return new User(name, email);
        }

    }

    public enum Validator{
        SUCCESS,
        BAD_NAME,
        BAD_EMAIL,
    }

    public static void main(String[] args) {
        User user = CombinatorPattern.User.of("simeao", "simeaolm@gmail.com");
        UserRegistryValidator
                .isEmailValid()
                .and(UserRegistryValidator.isValidName())
                .apply(user);
    }

}
