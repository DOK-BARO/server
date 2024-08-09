package kr.kro.dokbaro.server.core.account.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.account.application.port.input.command.DisableAccountUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CancelAccountController::class)
class CancelAccountControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var disableAccountUseCase: DisableAccountUseCase

	init {
		"회원 탈퇴를 수행한다" {
			val action = performPost(Path("/accounts/cancel"))

			action.andExpect(status().isNotImplemented)

			action.andDo(
				print(
					"account/cancel-account",
				),
			)
		}
	}
}