package de.tudresden.slr.model.mendeley.util;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * This class implements an ISchedulingRule which is used to prevent
 * multiple Jobs from running in a parallel manner.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MutexRule implements ISchedulingRule {
      public boolean isConflicting(ISchedulingRule rule) {
         return rule == this;
      }
      public boolean contains(ISchedulingRule rule) {
         return rule == this;
      }
   }