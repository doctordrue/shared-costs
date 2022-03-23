package org.doctordrue.sharedcosts.business.model.widget;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Stake;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
public class CostGroupDetails {

   private CostGroup group;
   private List<CostDetails> costs;

   public CostGroup getGroup() {
      return group;
   }

   public CostGroupDetails setGroup(CostGroup group) {
      this.group = group;
      return this;
   }

   public List<CostDetails> getCosts() {
      return costs;
   }

   public CostGroupDetails setCosts(List<CostDetails> costs) {
      this.costs = costs;
      return this;
   }
}
