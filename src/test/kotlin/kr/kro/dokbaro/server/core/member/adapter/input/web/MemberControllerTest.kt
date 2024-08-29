package kr.kro.dokbaro.server.core.member.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.member.adapter.input.web.dto.ModifyMemberRequest
import kr.kro.dokbaro.server.core.member.application.port.input.command.ModifyMemberUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MemberController::class)
class MemberControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var modifyMemberUseCase: ModifyMemberUseCase

	init {
		"member 수정을 수행한다" {
			every { modifyMemberUseCase.modify(any()) } returns Unit

			val request =
				ModifyMemberRequest(
					nickName = "nickname",
					email = "ddd@example.com",
					profileImage = "hello.png",
				)

			performPut(Path("/members/login-user"), request)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"member/modify-login-user",
						requestFields(
							fieldWithPath("nickName")
								.type(JsonFieldType.STRING)
								.description("별명 (optional)")
								.optional(),
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("email: 변경 시 메일 재인증 필요 (optional)"),
							fieldWithPath("profileImage")
								.type(JsonFieldType.STRING)
								.description("프로필 사진 (optional)")
								.optional(),
						),
					),
				)
		}
	}
}