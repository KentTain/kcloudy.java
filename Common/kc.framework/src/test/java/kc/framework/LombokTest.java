package kc.framework;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import kc.framework.base.ConfigEntity;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
//@ContextConfiguration()
//@ActiveProfiles(profiles = {Profiles.WEB_REST})
//@TestPropertySource("/config/rest.yml")
//@WebMvcTest(EntityController.class)
//@DirtiesContext
class LombokTest {
	@Test
	void testConfigLombok() throws Exception {

		ConfigEntity config = new ConfigEntity();
		config.setConfigId(1);
		assertEquals(1, config.getConfigId());
	}
}
