package kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import org.jooq.Configuration
import org.jooq.DSLContext

@PersistenceAdapterTest
class StudyGroupJooqRepositoryTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))
	})