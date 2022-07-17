import com.chaoshan.GatewayApplication;
import com.chaoshan.common.ModuleServers;
import com.chaoshan.common.SwaggerServers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @DATE: 2022/05/05 09:49
 * @Author: 小爽帅到拖网速
 */
@SpringBootTest(classes = GatewayApplication.class)
public class test {

  @Autowired
  private Environment env;

  @Test
  void test() {
    // SwaggerServers.getServers().forEach(a-> System.out.println(a.toString()));
    List<String> collect = SwaggerServers.getServers().stream().map(a -> a.getName()).collect(Collectors.toList());
    collect.forEach(System.out::println);
  }

  @Test
  void testModules() {
    List<String> modules = ModuleServers.getModules();
    modules.forEach(System.out::println);
    // System.out.println(env.getProperty("chaoshan.modules[0]"));
  }
}
