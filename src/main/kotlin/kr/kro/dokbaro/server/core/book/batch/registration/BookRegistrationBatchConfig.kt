package kr.kro.dokbaro.server.core.book.batch.registration

import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class BookRegistrationBatchConfig(
	private val dataSource: DataSource,
	private val jobRepository: JobRepository,
	private val transactionManager: PlatformTransactionManager,
)