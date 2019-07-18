package i5.las2peer.services.images;


import java.net.HttpURLConnection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import i5.las2peer.api.Context;
import i5.las2peer.api.ManualDeployment;
import i5.las2peer.api.ServiceException;
import i5.las2peer.api.logging.MonitoringEvent;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import i5.las2peer.services.images.database.DatabaseManager;
import java.sql.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import org.json.simple.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List; 
import java.util.ArrayList; 
import java.util.concurrent.TimeUnit;
 

/**
 *
 * ma-hossner-image-service
 *
 * This microservice was generated by the CAE (Community Application Editor). If you edit it, please
 * make sure to keep the general structure of the file and only add the body of the methods provided
 * in this main file. Private methods are also allowed, but any "deeper" functionality should be
 * outsourced to (imported) classes.
 *
 */
@ServicePath("images")
@ManualDeployment
public class Images extends RESTService {


  /*
   * Database configuration
   */
  private String jdbcDriverClassName;
  private String jdbcLogin;
  private String jdbcPass;
  private String jdbcUrl;
  private static DatabaseManager dbm;



  public Images() {
	super();
    // read and set properties values
    setFieldValues();
        // instantiate a database manager to handle database connection pooling and credentials
    dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl);
  }

  @Override
  public void initResources() {
	getResourceConfig().register(RootResource.class);
  }

  // //////////////////////////////////////////////////////////////////////////////////////
  // REST methods
  // //////////////////////////////////////////////////////////////////////////////////////

  @Api
  @SwaggerDefinition(
      info = @Info(title = "ma-hossner-image-service", version = "3",
          description = "Simple image hosting service.",
          termsOfService = "",
          contact = @Contact(name = "Philipp Hossner", email = "CAEAddress@gmail.com") ,
          license = @License(name = "BSD",
              url = "https://github.com/CAE-Community-Application-Editor/microservice-ma-hossner-image-service/blob/master/LICENSE.txt") ) )
  @Path("/")
  public static class RootResource {

    private final Images service = (Images) Context.getCurrent().getService();

      /**
   * 
   * getImages
   *
   * 
   *
   * 
   * @return Response 
   * 
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.TEXT_PLAIN)
  @ApiResponses(value = {
       @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "images")
  })
  @ApiOperation(value = "getImages", notes = " ")
  public Response getImages() {




    // service method invocations 
     
    // !!! BUG !!!
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // !!! ENDBUG !!! 

    ResultSet results;
    try {
      results = this.service.dbm.getConnection().createStatement().executeQuery("SELECT imageData FROM Images");
    } catch (SQLException e) {
      e.printStackTrace();
      return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).build();
    }





    // images
    boolean images_condition = true;
    if(images_condition) {
      JSONObject imagesResult = new JSONObject();

      List<String> imageList = new ArrayList<>();
      try {
        while (results.next()) {
          imageList.add(results.getString(1));
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      imagesResult.put("images", imageList);

      return Response.status(HttpURLConnection.HTTP_OK).entity(imagesResult.toJSONString()).build();
    }
    return null;
  }

  /**
   * 
   * addImage
   *
   * 
   * @param image  a JSONObject
   * 
   * @return Response 
   * 
   */
  @POST
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.TEXT_PLAIN)
  @ApiResponses(value = {
       @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "imageUploaded")
  })
  @ApiOperation(value = "addImage", notes = " ")
  public Response addImage(String image) {
    JSONObject image_JSON = (JSONObject) JSONValue.parse(image);




    // service method invocations
    try {
      PreparedStatement preparedStmt = this.service.dbm.getConnection().prepareStatement("INSERT INTO Images (imageData) VALUES (?)");
      preparedStmt.setString(1, (String) image_JSON.get("image"));
      preparedStmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }





    // imageUploaded
    boolean imageUploaded_condition = true;
    if(imageUploaded_condition) {
      JSONObject imageUploadedResult = new JSONObject();
      imageUploadedResult.put("msg", "upload successful");
      

      return Response.status(HttpURLConnection.HTTP_OK).entity(imageUploadedResult.toJSONString()).build();
    }
    return null;
  }



  }

  // //////////////////////////////////////////////////////////////////////////////////////
  // Service methods (for inter service calls)
  // //////////////////////////////////////////////////////////////////////////////////////
  
  

  // //////////////////////////////////////////////////////////////////////////////////////
  // Custom monitoring message descriptions (can be called via RMI)
  // //////////////////////////////////////////////////////////////////////////////////////

  public Map<String, String> getCustomMessageDescriptions() {
    Map<String, String> descriptions = new HashMap<>();
    
    return descriptions;
  }

}
