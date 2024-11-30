package kr.kro.dokbaro.server.core.studygroup.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllMyStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindStudyGroupDetailUseCase
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StudyGroupQueryController::class)
class StudyGroupQueryControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findAllMyStudyGroupUseCase: FindAllMyStudyGroupUseCase

	@MockkBean
	lateinit var findStudyGroupDetailUseCase: FindStudyGroupDetailUseCase

	init {
		"로그인한 사용자의 그룹 참여 목록을 보여준다" {
			every { findAllMyStudyGroupUseCase.findAll(any()) } returns
				listOf(
					StudyGroupSummary(1, "C 스터디", "ccc.png"),
					StudyGroupSummary(2, "JAVA 스터디", "ccc.png"),
					StudyGroupSummary(3, "모각코 합시다", "ccc.png"),
				)

			performGet(Path("/study-groups/my"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"study-group/get-my-study-group-list",
						responseFields(
							fieldWithPath("[].name").type(JsonFieldType.STRING).description("스터디 그룹 명"),
							fieldWithPath("[].profileImageUrl").type(JsonFieldType.STRING).description("스터디 그룹 프로필 사진"),
							fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("스터디 그룹 ID"),
						),
					),
				)
		}

		"스터디 그룹 정보를 조회한다" {
			every { findStudyGroupDetailUseCase.findStudyGroupDetailBy(any()) } returns
				StudyGroupDetail(
					name = "AI 연구 스터디",
					introduction = "인공지능과 머신러닝에 대해 함께 공부하는 스터디입니다.",
					profileImageUrl = "https://example.com/group-profile.jpg",
					studyMembers =
						listOf(
							StudyGroupDetail.StudyMember(
								id = 1L,
								nickname = "철수",
								profileImageUrl = "https://example.com/member1-profile.jpg",
								role = "LEADER",
							),
							StudyGroupDetail.StudyMember(
								id = 2L,
								nickname = "영희",
								profileImageUrl = "https://example.com/member2-profile.jpg",
								role = "MEMBER",
							),
							StudyGroupDetail.StudyMember(
								id = 3L,
								nickname = "민수",
								profileImageUrl = null,
								role = "MEMBER",
							),
						),
					inviteCode = "ABC123",
					id = 101L,
				)

			performGet(Path("/study-groups/{id}", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"study-group/get-study-group-detail",
						pathParameters(parameterWithName("id").description("study group ID")),
						responseFields(
							fieldWithPath("name").description("스터디 그룹의 이름"),
							fieldWithPath("introduction").description("스터디 그룹 소개").optional(),
							fieldWithPath("profileImageUrl").description("스터디 그룹 프로필 이미지의 URL").optional(),
							fieldWithPath("studyMembers").description("스터디 그룹의 멤버 목록"),
							fieldWithPath("studyMembers[].id").description("멤버의 고유 ID"),
							fieldWithPath("studyMembers[].nickname").description("멤버의 닉네임"),
							fieldWithPath("studyMembers[].profileImageUrl").description("멤버의 프로필 이미지 URL").optional(),
							fieldWithPath("studyMembers[].role").description("스터디 그룹 내 멤버의 역할 (LEADER, MEMBER)"),
							fieldWithPath("inviteCode").description("스터디 그룹에 참여할 수 있는 초대 코드"),
							fieldWithPath("id").description("스터디 그룹의 고유 ID"),
						),
					),
				)
		}
	}
}