import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.junit.Test
import org.scalatest.FunSuite

import org.apache.spark.sql.SparkSession


class SessionTest extends FunSuite with DataFrameSuiteBase {

  implicit val sparkImpl: SparkSession = spark

  @Test
  def simpleLookupTest {

    val homeDir = System.getProperty("user.home")
    val training = spark.read.format("libsvm")
      .load(s"$homeDir\\Documents\\GitHub\\sample_linear_regression_data.txt")
    println("completed simple lookup test")
  }

}
