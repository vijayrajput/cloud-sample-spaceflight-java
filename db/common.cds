// *********************************************************************************************************************
// REMOVE THIS FILE
//
// We use it from the 'spaceflight-model' package (see package.json)
// *********************************************************************************************************************
namespace common;

abstract entity Managed {
  key ID : UUID;
  createdAt  : DateTime @cds.on.insert: $now  @odata.on.insert: #now;
  createdBy  : User     @cds.on.insert: $user @odata.on.insert: #user;
}

type User : String(111);
