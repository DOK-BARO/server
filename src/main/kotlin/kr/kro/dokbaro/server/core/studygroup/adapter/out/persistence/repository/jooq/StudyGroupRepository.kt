package kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import org.jooq.DSLContext
import org.jooq.generated.tables.JStudyGroup
import org.jooq.generated.tables.JStudyGroupMember
import org.springframework.stereotype.Repository

@Repository
class StudyGroupRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val STUDY_GROUP = JStudyGroup.STUDY_GROUP
		private val STUDY_GROUP_MEMBER = JStudyGroupMember.STUDY_GROUP_MEMBER
	}

	fun insert(studyGroup: StudyGroup): Long {
		val studyGroupId: Long =
			dslContext
				.insertInto(
					STUDY_GROUP,
					STUDY_GROUP.NAME,
					STUDY_GROUP.INTRODUCTION,
					STUDY_GROUP.PROFILE_IMAGE_URL,
				).values(
					studyGroup.name,
					studyGroup.introduction.toByteArray(),
					studyGroup.profileImageUrl,
				).returningResult(STUDY_GROUP.ID)
				.fetchOneInto(Long::class.java)!!

		studyGroup.studyMembers.forEach {
			dslContext
				.insertInto(
					STUDY_GROUP_MEMBER,
					STUDY_GROUP_MEMBER.STUDY_GROUP_ID,
					STUDY_GROUP_MEMBER.MEMBER_ID,
					STUDY_GROUP_MEMBER.MEMBER_ROLE,
				).values(
					studyGroupId,
					it.memberId,
					it.role.name,
				).execute()
		}

		return studyGroupId
	}
}