package repositories.document

import conf.connection.DataConnection
import domain.Document.Document
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import repositories.Document.DocumentRepository
import services.Document.DocumentService

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by AidenP on 2016/12/13.
  */
class DocumentRepositoryTest extends FunSuite with BeforeAndAfterEach{
  implicit val keyspace = DataConnection.keySpace
  implicit val session = DataConnection.session

  override protected def beforeEach(): Unit = {
    //Create Table
    DocumentRepository.create.ifNotExists().future()
  }

  test("testSaveOrUpdate") {
    val document = Document(
      "test",
      Map("test"->"test"),
      "12",
      "test",
      "test",
      "www.test.com",
      "test-test-test",
      "test-test-test",
      "test",
      "test",
      "test")

    val result = Await.result(DocumentService.apply.createOrUpdate(document), 2.minutes)
    assert(result.isExhausted)
  }



  test("testGetDocument") {
    val result = Await.result(DocumentService.apply.getDocumentById("12"), 2.minutes)
    assert( result.head.description === "test")
  }

  override protected def afterEach(): Unit = {
    //Delete All records
    DocumentRepository.truncate().future()
  }
}
