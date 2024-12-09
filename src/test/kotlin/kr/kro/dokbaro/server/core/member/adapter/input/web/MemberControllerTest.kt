package kr.kro.dokbaro.server.core.member.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.member.adapter.input.web.dto.ModifyMemberRequest
import kr.kro.dokbaro.server.core.member.application.port.input.command.ModifyMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.WithdrawMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindMyAvatarUseCase
import kr.kro.dokbaro.server.core.member.query.MyAvatar
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(MemberController::class)
class MemberControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var modifyMemberUseCase: ModifyMemberUseCase

	@MockkBean
	lateinit var withdrawMemberUseCase: WithdrawMemberUseCase

	@MockkBean
	lateinit var findMyAvatarUseCase: FindMyAvatarUseCase

	init {
		"login한 member 정보를 수정을 수행한다" {
			every { modifyMemberUseCase.modify(any()) } returns Unit

			val request =
				ModifyMemberRequest(
					nickname = "nickname",
					email = "ddd@example.com",
					profileImage = "hello.png",
				)

			performPut(Path("/members/login-user"), request)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"member/modify-login-user",
						requestFields(
							fieldWithPath("nickname")
								.type(JsonFieldType.STRING)
								.description("별명 (optional)")
								.optional(),
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("email: 변경 시 메일 재인증 필요 (optional)"),
							fieldWithPath("profileImage")
								.type(JsonFieldType.STRING)
								.description("프로필 사진 URL (optional)")
								.optional(),
						),
					),
				)
		}

		"login한 member 정보를 가져온다" {
			every { findMyAvatarUseCase.findMyAvatar(any()) } returns
				MyAvatar(
					id = 12345L,
					certificationId = UUID.randomUUID(),
					nickname = "CoolCoder",
					email = "coolcoder@example.com",
					profileImage = "https://example.com/profile-image.png",
					role = listOf("USER", "ADMIN"),
				)

			performGet(Path("/members/login-user"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"member/get-login-user",
						responseFields(
							fieldWithPath("id")
								.type(JsonFieldType.NUMBER)
								.description("seq ID"),
							fieldWithPath("nickname")
								.type(JsonFieldType.STRING)
								.description("닉네임"),
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("이메일 주소"),
							fieldWithPath("profileImage")
								.type(JsonFieldType.STRING)
								.description("프로필 사진 URL"),
							fieldWithPath("certificationId")
								.type(JsonFieldType.STRING)
								.description("인증용 Id"),
							fieldWithPath("role")
								.type(JsonFieldType.ARRAY)
								.description("권한"),
						),
					),
				)
		}

		"회원 탈퇴를 진행한다" {
			every { withdrawMemberUseCase.withdrawBy(any()) } returns Unit

			performPost(Path("/members/withdraw"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"member/withdraw",
					),
				)
		}
	}
}