package kr.kro.dokbaro.server.core.studygroup.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.CreateStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.JoinStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.application.port.input.CreateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.DeleteStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.JoinStudyGroupUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StudyGroupController::class)
class StudyGroupControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createStudyGroupUseCase: CreateStudyGroupUseCase

	@MockkBean
	lateinit var joinStudyGroupUseCase: JoinStudyGroupUseCase

	@MockkBean
	lateinit var deleteStudyGroupUseCase: DeleteStudyGroupUseCase

	init {
		"스터디 그룹 생성을 수행한다" {
			every { createStudyGroupUseCase.create(any()) } returns 1

			val body =
				CreateStudyGroupRequest(
					"자바 스터디",
					"재밌어요 모두 함께해여",
					"profile.png",
				)

			performPost(Path("/study-groups"), body)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"study-group/create",
						requestFields(
							fieldWithPath("name").description("그룹명"),
							fieldWithPath("introduction").description("그룹 소개글 (optional)").optional(),
							fieldWithPath("profileImageUrl").description("그룹 프로필 사진 (optional)").optional(),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("스터디 그룹 ID"),
						),
					),
				)
		}

		"스터디 그룹에 참여한다" {
			every { joinStudyGroupUseCase.join(any()) } returns Unit

			val body = JoinStudyGroupRequest("ABC123")

			performPost(Path("/study-groups/join"), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"study-group/join",
						requestFields(fieldWithPath("inviteCode").description("초대 코드")),
					),
				)
		}

		"스터디 그룹을 삭제한다" {
			every { deleteStudyGroupUseCase.deleteStudyGroup(any(), any()) } returns Unit

			performDelete(Path("/study-groups/{id}", "1"))
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"study-group/delete",
						pathParameters(parameterWithName("id").description("삭제할 스터디 그룹 ID")),
					),
				)
		}
	}
}