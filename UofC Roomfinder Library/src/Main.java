import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uofc.roomfinder.entities.routing.Route;
import com.uofc.roomfinder.entities.routing.RoutePoint;
import com.uofc.roomfinder.util.gson.RouteJsonSerializer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Route newRoute = new Route();

		RoutePoint start = new RoutePoint(701192.8861, 5662659.7696, 0.0);
		RoutePoint end = new RoutePoint(701012.8757, 5662665.3092, 16.0);

		newRoute = Route.getSoapRoute(start, end, "IndoorCost");

		Gson gson = new GsonBuilder().registerTypeAdapter(Route.class, new RouteJsonSerializer()).setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(newRoute);

		System.out.println(json);

	}

}
