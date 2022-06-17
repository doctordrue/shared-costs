package org.doctordrue.telegram.bot.api.session;

/**
 * @param <Key>     session key type<br>
 * @param <Session> session type
 * @author Andrey_Barantsev
 * 5/19/2022
 **/
public interface ISessionHolder<Key, Session> {

   /**
    * Verify if session for specified <b>key</b> already exists
    *
    * @param key
    * @return
    */
   boolean hasSession(Key key);

   /**
    * Get session from the session storage by provided key
    *
    * @param key
    * @return
    */
   Session getSession(Key key);

   /**
    * Persist session if necessary
    *
    * @param session
    */
   void persist(Session session);

   void removeSession(Key key);

}
