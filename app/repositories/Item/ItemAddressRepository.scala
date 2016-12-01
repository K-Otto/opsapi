package repositories.Item

import com.datastax.driver.core.Row
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.column.PrimitiveColumn
import com.websudos.phantom.dsl._
import com.websudos.phantom.keys.PartitionKey
import com.websudos.phantom.reactivestreams._
import conf.connection.DataConnection
import domain.Item.ItemAddress

import scala.concurrent.Future

/**
  * Created by AidenP on 2016/11/30.
  */
class ItemAddressRepository extends CassandraTable[ItemAddressRepository, ItemAddress]{
  object ItemId extends StringColumn(this) with PartitionKey[String]
  object AddressId extends  StringColumn(this)

  override def fromRow(r: Row):ItemAddress = {
    ItemAddress(
      ItemId(r),
      AddressId(r)

    )

  }

}

object ItemAddressRepository extends ItemAddressRepository with RootConnector {
  override lazy val tableName = "schedule"

  override implicit def space: KeySpace = DataConnection.keySpace

  override implicit def session: Session = DataConnection.session

  def save(itemAddress: ItemAddress): Future[ResultSet] = {
    insert
      .value(_.ItemId, itemAddress.ItemId)
      .value(_.AddressId, itemAddress.AddressId)
      .future()
  }

  def getItemAddressById(ItemId: String): Future[Option[ItemAddress]] = {
    select.where(_.ItemId eqs ItemId).one()
  }

  def getAllItemAddresss: Future[Seq[ItemAddress]] = {
    select.fetchEnumerator() run Iteratee.collect()
  }

  def getItemAddress(ItemId: String): Future[Seq[ItemAddress]] = {
    select.where(_.ItemId eqs ItemId).fetchEnumerator() run Iteratee.collect()
  }

  def deleteById(ItemId: String): Future[ResultSet] = {
    delete.where(_.ItemId eqs ItemId).future()
  }
}
