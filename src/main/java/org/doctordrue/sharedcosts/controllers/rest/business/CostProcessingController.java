package org.doctordrue.sharedcosts.controllers.rest.business;

import org.doctordrue.sharedcosts.business.model.processing.CostProcessingResult;
import org.doctordrue.sharedcosts.business.model.processing.CostSplitProcessingInputData;
import org.doctordrue.sharedcosts.business.services.processing.CostProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrey_Barantsev
 * 3/18/2022
 **/
@RestController
@RequestMapping("/api/v1/process/costs")
public class CostProcessingController {

   @Autowired
   private CostProcessingService costProcessingService;

   @PostMapping("/split")
   public CostProcessingResult process(@RequestBody CostSplitProcessingInputData processingInputData) {
      return this.costProcessingService.processCost(
              processingInputData.getName(),
              processingInputData.getCostGroupId(),
              processingInputData.getCurrencyShortName(),
              processingInputData.getAmount(),
              processingInputData.getStakeholdersIds(),
              processingInputData.getPayersIds(),
              processingInputData.getTimestamp());
   }


}
