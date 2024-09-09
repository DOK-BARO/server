package kr.kro.dokbaro.server.core.studygroup.adapter.input

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.dto.CreateStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.application.port.input.CreateStudyGroupUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StudyGroupController::class)
class StudyGroupControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createStudyGroupUseCase: CreateStudyGroupUseCase

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
							fieldWithPath("introduction").description("그룹 소개글"),
							fieldWithPath("profileImageUrl").description("그룹 프로필 사진"),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("스터디 그룹 ID"),
						),
					),
				)
		}
	}
}