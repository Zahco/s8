package fr.rouen.mastergil.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Created by spaurgeo on 06/03/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private MailService mailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private MyLogger log;

    @InjectMocks
    private UserService userService;


    @Test
    public void createUserInformation() throws Exception {
        //Given
        String password = "";
        String login = "";
        String firstName = "";
        String lastName = "";
        String email = "";
        String langKey = "";

        //When
        User user = userService.createUserInformation(login, password, firstName, lastName, email, langKey);

        //Then
        Mockito.verify(authorityRepository, Mockito.times(1)).findOne(AuthoritiesConstants.USER);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(password);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(log, Mockito.times(1)).debug("Created Information for User: {}", user);

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getLogin()).isEqualTo(login);
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getLangKey()).isEqualTo(langKey);
        assertThat(user.getPassword()).isEqualTo(passwordEncoder.encode(password));
    }

    @Test
    public void activateRegistration() throws Exception {
        //Given
        String key = RandomUtil.generateActivationKey();
        User user = userService.createUserInformation("", "","","","","");
        Optional<User> optional = Optional.of(user);
        Mockito.when(userRepository.findOneByActivationKey(key)).thenReturn(optional);

        //When
        userService.activateRegistration(key);

        //Then
        Mockito.verify(log, Mockito.times(1)).debug("Activating user for activation key {}", key);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByActivationKey(key);
        Mockito.verify(userRepository, Mockito.times(2)).save(user);
        Mockito.verify(log, Mockito.times(1)).debug("Activated user: {}", user);

        assertThat(user.isActivated()).isTrue();
        assertThat(user.getActivationKey()).isEqualTo(null);
    }

    @Test
    public void completePasswordReset() throws Exception {
        //Given
        String key = RandomUtil.generateActivationKey();
        String password = "a";
        User user = userService.createUserInformation("", "","","","","");
        Optional<User> optional = Optional.of(user);
        Mockito.when(userRepository.findOneByResetKey(key)).thenReturn(optional);
        user.setResetDate(ZonedDateTime.now());

        //When
        userService.completePasswordReset(password, key);

        //Then
        Mockito.verify(log, Mockito.times(1)).debug("Reset user password for reset key {}", key);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByResetKey(key);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(password);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("");
        Mockito.verify(userRepository, Mockito.times(2)).save(user);

        assertThat(user.getPassword()).isEqualTo(passwordEncoder.encode(password));
        assertThat(user.getResetDate()).isEqualTo(null);
        assertThat(user.getResetKey()).isEqualTo(null);
    }

    @Test
    public void requestPasswordReset() throws Exception {
        //Given
        String mail = "";
        User user = userService.createUserInformation("", "","","","","");
        Optional<User> optional = Optional.of(user);
        Mockito.when(userRepository.findOneByEmail(mail)).thenReturn(optional);
        user.setActivated(true);
        String key = user.getResetKey();

        //When
        userService.requestPasswordReset(mail);

        //Then
        Mockito.verify(userRepository, Mockito.times(1)).findOneByEmail(mail);
        Mockito.verify(userRepository, Mockito.times(2)).save(user);

        assertThat(user.getResetKey()).isNotEqualTo(key);
        assertThat(user.getResetDate()).isGreaterThan(ZonedDateTime.now().minusHours(1));
    }

    @Test
    public void checkUserActivatedStatus() throws Exception {
        //Given
        User user = userService.createUserInformation("", "","","","","");
        List<User> list = new ArrayList<User>();
        list.add(user);
        Mockito.when(userRepository.findAll()).thenReturn(list);
        user.setActivated(true);

        //When
        userService.checkUserStatus();

        //Then
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verify(log, Mockito.times(1)).debug("user {} is activated", user);
    }

    @Test
    public void checkUserNotActivatedStatus() throws Exception {
        //Given
        User user = userService.createUserInformation("", "","","","","");
        List<User> list = new ArrayList<User>();
        list.add(user);
        Mockito.when(userRepository.findAll()).thenReturn(list);
        user.setActivated(false);

        //When
        userService.checkUserStatus();

        //Then
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verify(log, Mockito.times(1)).debug("user {} is not activated", user);
    }

    @Test
    public void checkLogResetDatePastStatus() throws Exception {
        //Given
        User user = userService.createUserInformation("", "","","","","");
        List<User> list = new ArrayList<User>();
        list.add(user);
        Mockito.when(userRepository.findAll()).thenReturn(list);
        user.setActivated(false);
        user.setResetDate(ZonedDateTime.now().minusHours(26));

        //When
        userService.checkUserStatus();

        //Then
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verify(log, Mockito.times(1)).debug("reset date is past for user {}", user);
    }

    @Test
    public void checkLogResetDateStatus() throws Exception {
        //Given
        User user = userService.createUserInformation("", "","","","","");
        List<User> list = new ArrayList<User>();
        list.add(user);
        Mockito.when(userRepository.findAll()).thenReturn(list);
        user.setActivated(false);
        user.setResetDate(ZonedDateTime.now().minusHours(3));

        //When
        userService.checkUserStatus();

        //Then
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verify(log, Mockito.times(1)).debug("reset date for {}", user);
    }

}