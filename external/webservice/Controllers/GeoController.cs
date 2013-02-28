using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Configuration;

using Newtonsoft.Json;

using AiMetrix.BusinessObject.Security;
using AiMetrix.BusinessObject.Inventory;

namespace nsandroid.webservice.Controllers
{
    public class GeoController : AsyncController
    {
        public void SaveObjectPositionAsync(Guid NSObjectID, double Latitude, double Longitude)
        {
            SqlConnection sqlconn = null;
            SqlCommand sqlcmd = null;
            
            try {
                AsyncManager.OutstandingOperations.Increment();

                sqlconn = new SqlConnection(ConfigurationManager.AppSettings["Database"]);
                sqlconn.Open();

                sqlcmd = new SqlCommand("GeoSpatialPosition_Save", sqlconn);

                sqlcmd.Parameters.Add(new SqlParameter("@ContainmentID", NSObjectID));
                sqlcmd.Parameters.Add(new SqlParameter("@ContainmentTypeID", 1));
                sqlcmd.Parameters.Add(new SqlParameter("@Latitude", Latitude));
                sqlcmd.Parameters.Add(new SqlParameter("@Longitude", Longitude));

                sqlcmd.ExecuteNonQuery();

                AsyncManager.Parameters["ReturnValue"] = true;
            } catch (Exception ex) {
                ex = ex.GetBaseException();
                AsyncManager.Parameters["OriginalException"] = ex;
                AsyncManager.Parameters["ReturnValue"] = false;
            } finally {
                if (sqlcmd != null) { sqlcmd.Dispose(); }
                if (sqlconn != null) { sqlconn.Dispose(); }

                AsyncManager.OutstandingOperations.Decrement();
            }
        }

        public ActionResult SaveObjectPositionCompleted(object ReturnValue, Exception OriginalException)
        {
            if (OriginalException != null) return new HttpStatusCodeResult(500, OriginalException.Message);

            ContentResult contentReturned = new ContentResult();
            contentReturned.Content = JsonConvert.SerializeObject(ReturnValue);
            contentReturned.ContentType = "application/json";
            return contentReturned;
        }
    }
}