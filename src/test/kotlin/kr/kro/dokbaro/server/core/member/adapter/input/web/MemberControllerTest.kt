package kr.kro.dokbaro.server.core.member.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.member.adapter.input.web.dto.ModifyMemberRequest
import kr.kro.dokbaro.server.core.member.application.port.input.command.ModifyMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.dto.CertificatedMember
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.member.domain.Role
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import kotlin.random.Random

@WebMvcTest(MemberController::class)
class MemberControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var modifyMemberUseCase: ModifyMemberUseCase

	@MockkBean
	lateinit var findCertificatedMemberUseCase: FindCertificatedMemberUseCase

	init {
		"login한 member 정보를 수정을 수행한다" {
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
								.description("프로필 사진 URL (optional)")
								.optional(),
						),
					),
				)
		}

		"login한 member 정보를 가져온다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns
				CertificatedMember(
					nickName = "nickname",
					email = "asdf@gmail.com",
					profileImage = "image.png",
					certificationId = UUID.randomUUID(),
					roles = setOf(Role.GUEST),
					id = Random.nextLong(),
				)

			performGet(Path("/members/login-user"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"member/get-login-user",
						responseFields(
							fieldWithPath("nickName")
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
							fieldWithPath("roles")
								.type(JsonFieldType.ARRAY)
								.description("권한"),
							fieldWithPath("id")
								.type(JsonFieldType.NUMBER)
								.description("seq ID"),
						),
					),
				)
		}
	}
}