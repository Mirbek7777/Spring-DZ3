package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

  private final String GET = "GET";
  private final String POST = "POST";
  private final String DELETE = "DELETE";
  private final String PATH = "/api/posts";
  private final String PATH_WITH_NUMBER = "/api/posts/\\d+";
  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {

    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals(GET) && path.equals(PATH)) {
        controller.all(resp);
        return;
      }
      if (method.equals(GET) && path.matches(PATH_WITH_NUMBER)) {

        final var id = parseNumber(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals(POST) && path.equals(PATH)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE) && path.matches(PATH_WITH_NUMBER)) {

        final var id = parseNumber(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private static Long parseNumber(String path){
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}
