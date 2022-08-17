package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

  private final Clock clock;
  private final ProfilingState profilingState;
  private final Object obj;

  // TODO: You will need to add more instance fields and constructor arguments to this class.
  ProfilingMethodInterceptor(Clock clock, ProfilingState profilingState, Object obj) {

    this.clock = Objects.requireNonNull(clock);
    this.profilingState = Objects.requireNonNull(profilingState);
    this.obj = Objects.requireNonNull(obj);

  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
    // TODO: This method interceptor should inspect the called method to see if it is a profiled
    //       method. For profiled methods, the interceptor should record the start time, then
    //       invoke the method using the object that is being profiled. Finally, for profiled
    //       methods, the interceptor should record how long the method call took, using the
    //       ProfilingState methods.

    Instant startTime = null; // Get instant Object of Clock
    Object ProfiledObj = null;
    boolean hasAnnotation = false;

    if (method.getAnnotation(Profiled.class) != null) {
      hasAnnotation = true; // return ProfiledObj = null
    }

    startTime = clock.instant(); // Get the specific time and output likes: 2018-08-21T05:31:10.662Z

    try {
      ProfiledObj = method.invoke(obj, args); //Invoke the method using the obj that is profiled
    } catch(InvocationTargetException e) {
      throw e.getTargetException();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } finally {
      if (hasAnnotation) {
        Duration elapsed = Duration.between(startTime, clock.instant());
        profilingState.record(obj.getClass(),method,elapsed);
      }
    }



    return ProfiledObj;
  }
}
