package kr.kro.dokbaro.server.core.studygroup.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.application.port.out.InsertStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup

@PersistenceAdapter
class StudyGroupPersistenceAdapter(
	private val studyGroupRepository: StudyGroupRepository,
) : InsertStudyGroupPort {
	override fun insert(studyGroup: StudyGroup): Long = studyGroupRepository.insert(studyGroup)
}