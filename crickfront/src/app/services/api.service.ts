import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient:HttpClient) {

   }

   getLiveScore(){
    return this.httpClient.get(`${environment.apiUrl}/cricket/live`)
   }

   getAllMatches(){
    return this.httpClient.get(`${environment.apiUrl}/cricket`)
   }
   getICCRankingTable(){
    return this.httpClient.get(`${environment.apiUrl}/cricket/ranking`)
   }
}
