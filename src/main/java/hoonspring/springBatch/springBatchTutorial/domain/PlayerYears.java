package hoonspring.springBatch.springBatchTutorial.domain;

import lombok.Data;

import java.time.Year;

@Data
public class PlayerYears {

    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
    private int yearExperience;

    public PlayerYears(Player player) {
        this.ID = player.getID();
        this.lastName = player.getLastName();
        this.firstName = player.getFirstName();
        this.position = player.getPosition();
        this.birthYear = player.getBirthYear();
        this.debutYear = player.getDebutYear();
        this.yearExperience = Year.now().getValue() - player.getDebutYear();
    }
}
