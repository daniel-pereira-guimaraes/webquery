package webquery;

import static spark.Spark.get;
import static spark.Spark.post;

import webquery.config.Environment;
import webquery.controller.WebQueryController;
import webquery.repository.WebQueryRepository;
import webquery.repository.impl.WebQueryRepositoryImpl;
import webquery.service.WebPageService;
import webquery.service.WebQueryService;
import webquery.service.impl.WebPageServiceImpl;
import webquery.service.impl.WebQueryServiceImpl;

/**
 * API entry point, which defines the supported endpoints.
 * 
 * @author Daniel
 *
 */
public class Main {
	
	private static final Environment environment = new Environment();
	private static final WebQueryRepository webQueryRepository = new WebQueryRepositoryImpl();
	private static final WebPageService webPageService = new WebPageServiceImpl(environment);
	private static final WebQueryService webQueryService = new WebQueryServiceImpl(environment, 
			webQueryRepository, webPageService);
	private static final WebQueryController webQueryController = new WebQueryController(webQueryService);
	
    public static void main(String[] args) {
    	get("/crawl/:id", webQueryController::getQuery);
        post("/crawl", webQueryController::startQuery);
    }
    
}
