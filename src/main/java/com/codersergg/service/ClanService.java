package com.codersergg.service;

import com.codersergg.model.Clan;
import com.codersergg.monitoring.model.GoldValues;
import java.sql.SQLException;
import java.util.Optional;

// Есть сервис, посвященный кланам.
// Да это выглядит как 'репозиторий'.
// Но это сервис, просто все остальные методы не нужны для примера
public interface ClanService {

  Optional<Clan> getClan(long clanId) throws SQLException, InterruptedException;

  void addClan(Clan clan) throws SQLException, InterruptedException;

  GoldValues changeClansGold(long clanId, int gold) throws SQLException, InterruptedException;
}
