package com.pri.architecture_boilerplate.db

import androidx.room.Dao

@Dao
interface MovieDao {
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTicket(ticketList: List<Ticket>)

    @Query("SELECT * FROM tickets")
    fun loadAllMovie(): LiveData<List<Ticket>>

    @Query("SELECT * FROM tickets where userName = :userName AND ticketStatus= :ticketStatus")
    fun loadAllTicketByUserName(userName: String?, ticketStatus: String?): LiveData<List<Ticket>>


    @Query("SELECT COUNT(ticketMasterId) FROM tickets WHERE userName = :userName AND ticketStatus = :ticketStatus")
    fun getCount(userName: String, ticketStatus: String): Int?

    @Query("DELETE from tickets")
    fun deleteAllTicket()*/
}