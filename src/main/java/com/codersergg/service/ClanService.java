package com.codersergg.service;

import com.codersergg.model.Clan;
import java.sql.SQLException;
import java.util.Optional;

// Есть сервис, посвященный кланам.
// Да это выглядит как 'репозиторий'.
// Но это сервис, просто все остальные методы не нужны для примера
public interface ClanService {

  Optional<Clan> getClan(long clanId) throws SQLException, InterruptedException;

  void addClan(Clan build) throws SQLException, InterruptedException;

  void changeClansGold(long clanId, int gold) throws SQLException, InterruptedException;
}