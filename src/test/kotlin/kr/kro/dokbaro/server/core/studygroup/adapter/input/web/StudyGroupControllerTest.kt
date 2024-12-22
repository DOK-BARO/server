package kr.kro.dokbaro.server.core.studygroup.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.ChangeStudyGroupLeaderRequest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.CreateStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.JoinStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.UpdateStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.WithdrawStudyGroupMemberRequest
import kr.kro.dokbaro.server.core.studygroup.application.port.input.ChangeStudyGroupLeaderUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.CreateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.DeleteStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.JoinStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.UpdateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.WithdrawStudyGroupMemberUseCase
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

	@MockkBean
	lateinit var updateStudyGroupUseCase: UpdateStudyGroupUseCase

	@MockkBean
	lateinit var changeStudyGroupLeaderUseCase: ChangeStudyGroupLeaderUseCase

	@MockkBean
	lateinit var withdrawStudyGroupMemberUseCase: WithdrawStudyGroupMemberUseCase

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

		"스터디 그룹 정보를 수정한다" {
			every { updateStudyGroupUseCase.update(any(), any()) } returns Unit

			val body =
				UpdateStudyGroupRequest(
					name = "알고리즘 스터디",
					introduction = "매주 수요일 저녁 8시에 모여서 알고리즘 문제를 함께 풀어보는 스터디입니다.",
					profileImageUrl = "https://example.com/images/algorithm-study-profile.jpg",
				)
			performPut(Path("/study-groups/{id}", "1"), body)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"study-group/update",
						pathParameters(parameterWithName("id").description("study group ID")),
						requestFields(
							fieldWithPath("name")
								.type(JsonFieldType.STRING)
								.description("스터디 그룹 이름"),
							fieldWithPath("introduction")
								.type(JsonFieldType.STRING)
								.description("스터디 그룹 소개")
								.optional(),
							fieldWithPath("profileImageUrl")
								.type(JsonFieldType.STRING)
								.description("프로필 이미지 URL")
								.optional(),
						),
					),
				)
		}

		"스터디 그룹 리더를 변경한다" {
			every { changeStudyGroupLeaderUseCase.changeStudyGroupLeader(any(), any()) } returns Unit

			val body = ChangeStudyGroupLeaderRequest(1)
			performPost(Path("/study-groups/{id}/change-leader", "1"), body)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"study-group/change-leader",
						pathParameters(parameterWithName("id").description("스터디 그룹 ID")),
						requestFields(
							fieldWithPath("newLeaderId").type(JsonFieldType.NUMBER).description("새로운 leader의 memberId"),
						),
					),
				)
		}

		"스터디 그룹 탈퇴(강제탈퇴)를 진행한다" {
			every { withdrawStudyGroupMemberUseCase.withdraw(any(), any()) } returns Unit

			val body = WithdrawStudyGroupMemberRequest(1)
			performPost(Path("/study-groups/{id}/withdraw", "1"), body)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"study-group/withdraw",
						pathParameters(parameterWithName("id").description("스터디 그룹 ID")),
						requestFields(
							fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("탈퇴할 member의 ID"),
						),
					),
				)
		}
	}
}