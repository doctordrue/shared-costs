package org.doctordrue.sharedcosts.data.entities;

import java.util.List;
import java.util.Set;

/**
 * @author Andrey_Barantsev
 * 6/21/2022
 **/
public interface ISharedAmount<T extends IOwnedAmount> extends IMoneyAmount {

   Set<Person> getPeople();

   List<T> getFlatAmounts();
}
