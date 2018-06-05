package edu.cornell.cac.sbh.core.openstack

import edu.cornell.cac.sbh.Util._
import java.util.Properties

import org.jclouds.ContextBuilder
import org.jclouds.Constants._
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule
import org.jclouds.openstack.keystone.config.KeystoneProperties
import org.jclouds.openstack.nova.v2_0._
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

//TODO: remove the hardcoding of these elelments and read from PureConfig
//TODO: https://github.com/pureconfig/pureconfig
case class OpenRC(
  OS_AUTH_URL: String = "http://localhost:5001/v3",
  OS_PROJECT_NAME: String = "MYPROJ",
  OS_PROJECT_DOMAIN_NAME: String = "my_proj_domain",
  //OS_USER_DOMAIN_NAME: String = "my_user_domain",
  OS_USERNAME: String = "myusername",
  IDENTITY_API_VERSION: Int = 3,
){

  /**
    * Note: for security reasons, this is a `def` and does not return a String
    * (heap value) dependent on GC to remove.
    * @return
    */
  private def OS_PASSWORD: Array[Char] = {
      println(
        "Please enter your OpenStack Password " +
        s"for project $OS_PROJECT_NAME as user $OS_USERNAME: "
      )
      print("Password> ")
    //FIXME: use readPassword once working in REPL
    //readLine(None) //FIXME DEBUG: hardcoding password for testing:
    "foobar".toCharArray
    }
}

object OpenRC{

  val logger: Logger = LoggerFactory.getLogger(classOf[OpenRC])
  def connect(openrc: OpenRC): NovaApi = {
    val overrides = new Properties()
    overrides.put(KeystoneProperties.KEYSTONE_VERSION, s"${openrc.IDENTITY_API_VERSION}")
    overrides.put(KeystoneProperties.SCOPE, s"project:${openrc.OS_PROJECT_NAME}")
    // DEBUG only:
    overrides.put(LOGGER_HTTP_HEADERS, "INFO")
    overrides.put(LOGGER_HTTP_WIRE, "INFO")
    overrides.put(PROPERTY_LOGGER_WIRE_LOG_SENSITIVE_INFO, "true")

    val openrcMods = List(new SLF4JLoggingModule)
    logger.info("Hello from slf4j")

    ContextBuilder.newBuilder("openstack-nova")
      .endpoint(openrc.OS_AUTH_URL)
      .credentials(
        s"${openrc.OS_PROJECT_DOMAIN_NAME}:${openrc.OS_USERNAME}", openrc.OS_PASSWORD.toString
      )
      .modules(openrcMods.asJava)
      .overrides(overrides)
      .buildApi(classOf[NovaApi])

  }
}