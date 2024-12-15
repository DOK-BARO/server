package kr.kro.dokbaro.server.core.studygroup.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.application.port.out.DeleteStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.InsertStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.LoadStudyGroupByInviteCodePort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.UpdateStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup

@PersistenceAdapter
class StudyGroupPersistenceAdapter(
	private val studyGroupRepository: StudyGroupRepository,
) : InsertStudyGroupPort,
	LoadStudyGroupByInviteCodePort,
	UpdateStudyGroupPort,
	DeleteStudyGroupPort {
	override fun insert(studyGroup: StudyGroup): Long = studyGroupRepository.insert(studyGroup)

	override fun findByInviteCode(inviteCode: String): StudyGroup? = studyGroupRepository.findByInviteCode(inviteCode)

	override fun update(studyGroup: StudyGroup) {
		studyGroupRepository.update(studyGroup)
	}

	override fun deleteStudyGroup(id: Long) {
		studyGroupRepository.delete(id)
	}
}