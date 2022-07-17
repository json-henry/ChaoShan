import com.chaoshan.AuthApplication;
import com.chaoshan.service.impl.IUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @DATE: 2022/04/28 14:42
 * @Author: 小爽帅到拖网速
 */

@SpringBootTest(classes = AuthApplication.class)
public class test {

  @Autowired
  private IUserServiceImpl userService;

  @Test
  public void test() {
    System.out.println(userService.removeById(Long.valueOf("1524210656539652098")));
  }
}
