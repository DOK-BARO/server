package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllMyStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindStudyGroupDetailByInviteCodeUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindStudyGroupDetailUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.out.CountStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupCollectionPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupDetailPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupMemberCollectionPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.CountStudyGroupCondition
import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.FindStudyGroupCondition
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.NotFoundStudyGroupException
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import kr.kro.dokbaro.server.core.studygroup.query.sort.MyStudyGroupSortKeyword
import org.springframework.stereotype.Service

@Service
class StudyGroupQueryService(
	private val readStudyGroupCollectionPort: ReadStudyGroupCollectionPort,
	private val readStudyGroupMemberCollectionPort: ReadStudyGroupMemberCollectionPort,
	private val findStudyGroupDetailPort: ReadStudyGroupDetailPort,
	private val countStudyGroupPort: CountStudyGroupPort,
) : FindAllMyStudyGroupUseCase,
	FindAllStudyGroupMembersUseCase,
	FindStudyGroupDetailUseCase,
	FindStudyGroupDetailByInviteCodeUseCase {
	override fun findAll(
		memberId: Long,
		pageOption: PageOption<MyStudyGroupSortKeyword>,
	): PageResponse<StudyGroupSummary> {
		val totalCount =
			countStudyGroupPort.countBy(
				CountStudyGroupCondition(memberId = memberId),
			)

		val data: Collection<StudyGroupSummary> =
			readStudyGroupCollectionPort.findAllByStudyMemberId(memberId, pageOption)

		return PageResponse.of(
			totalElementCount = totalCount,
			pageSize = pageOption.size,
			data = data,
		)
	}

	override fun findAllStudyGroupMembers(id: Long): Collection<StudyGroupMemberResult> =
		readStudyGroupMemberCollectionPort.findAllStudyGroupMembers(id)

	override fun findStudyGroupDetailBy(id: Long): StudyGroupDetail =
		findStudyGroupDetailPort.findStudyGroupDetailBy(FindStudyGroupCondition(id = id))
			?: throw NotFoundStudyGroupException()

	override fun findStudyGroupDetailByInviteCode(inviteCode: String): StudyGroupDetail =
		findStudyGroupDetailPort.findStudyGroupDetailBy(FindStudyGroupCondition(inviteCode = inviteCode))
			?: throw NotFoundStudyGroupException()
}