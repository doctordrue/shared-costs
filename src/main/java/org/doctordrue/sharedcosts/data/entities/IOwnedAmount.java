package org.doctordrue.sharedcosts.data.entities;

/**
 * @author Andrey_Barantsev
 * 4/5/2022
 **/
public interface IOwnedAmount extends IMoneyAmount {

   Person getPerson();

}
