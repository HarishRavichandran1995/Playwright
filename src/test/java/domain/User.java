package domain;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record User(
        String first_name,
        String last_name,
        Address address,
        String phone,
        String dob,
        String password,
        String email
) {

    public static User randomUser() {
        Faker faker = new Faker();
        int year = faker.number().numberBetween(1970, 2000);
        int month = faker.number().numberBetween(1, 12);
        int day = faker.number().numberBetween(1, 28);
        LocalDate date = LocalDate.of(year, month, day);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new User(
                faker.name().firstName(),
                faker.name().lastName(),
                new Address(faker.address().streetName(),
                        faker.address().city(),
                        faker.address().state(),
                faker.address().country(),
                        faker.address().postcode()),
                faker.phoneNumber().phoneNumber(),
                formattedDate,
                "Az1234$!3",
                faker.internet().emailAddress());
    }

    public User withPassword(String password) {
        return new User(
                this.first_name,
                this.last_name,
                this.address,
                this.phone,
                this.dob,
                password,
                this.email
        );
    }
}
