package kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.core.studygroup.domain.InviteCode
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JStudyGroupMember
import org.jooq.generated.tables.records.StudyGroupRecord
import org.springframework.stereotype.Component

@Component
class StudyGroupMapper {
	companion object {
		private val STUDY_GROUP_MEMBER = JStudyGroupMember.STUDY_GROUP_MEMBER
	}

	fun recordToStudyGroup(record: Map<StudyGroupRecord, Result<Record>>): StudyGroup? =
		record
			.map { (group, members) ->
				StudyGroup(
					name = group.name,
					introduction = group.introduction.toString(Charsets.UTF_8),
					profileImageUrl = group.profileImageUrl,
					studyMembers =
						members
							.map {
								StudyMember(
									it.getValue(STUDY_GROUP_MEMBER.MEMBER_ID),
									StudyMemberRole.valueOf(it.getValue(STUDY_GROUP_MEMBER.MEMBER_ROLE)),
								)
							}.toMutableSet(),
					inviteCode = InviteCode(group.inviteCode),
					id = group.id,
				)
			}.firstOrNull()

	fun toStudyGroupSummary(record: Result<StudyGroupRecord>): Collection<StudyGroupSummary> =
		record.map {
			StudyGroupSummary(
				it.name,
				it.profileImageUrl,
				it.id,
			)
		}
}