import { Injectable } from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, of, tap, throwError} from 'rxjs';
import {AuthResponse} from '../Model/interface/auth';
import { AuthUtilsService } from './auth-utils.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private creditUrl = 'http://localhost:9091/api/credit-score';
  creditResponse: any;
  registerUrl = 'http://localhost:9091/api/register'
  private roles: Array<string> = [];
  private apiUrl = 'http://localhost:9091/api/login/loginUser';
  private userUrl = 'http://localhost:9091/api/user';
  private baseUrl = 'http://localhost:9091/api/credit-reports';
  private activityUrl = 'http://localhost:9091/api/activity';
  constructor(private http: HttpClient, private router: Router, private authUtilsService: AuthUtilsService) { }

  login(userEmail: string, userPassword: string): Observable<AuthResponse | null> {
    return this.http.post<AuthResponse>(`${this.apiUrl}`, { userEmail, userPassword }, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      tap(res => {
        if (res && res.data) {
          localStorage.setItem('token', res.data);
        }
      }),
      catchError(error => {
        console.error('Login error:', error);
        return of(null);
      })
    );
  }
  register(formValue : any) : Observable<null> {
    console.log(formValue);
    return this.http.post<null>(`${this.registerUrl}/registerUser`,formValue);
  }
  getPendingUsers() {
    return this.http.get<any>(`${this.registerUrl}/pendingApprovalUserList`);
  }

  // approveUser(userEmail: string) {
  //   return this.http.post<AuthResponse>(`${this.registerUrl}/approve/${userEmail}`,{});
  // }
  predictCreditScore(inputData: any): Observable<any> {
    return this.http.post(`${this.creditUrl}/predict`, inputData);
  }

  setCreditResponse(response: any) {
    this.creditResponse = response;
  }

  getCreditResponse() {
    return this.creditResponse;
  }
  getCreditHistory(userEmail: string) {
    return this.http.get<AuthResponse>(`${this.creditUrl}/history/${userEmail}`);
  }

  approveUser(targetUserEmail: string) {
    const performedByEmail = localStorage.getItem('userEmail');
    const payload = {
      targetUserEmail: targetUserEmail,
      performedByEmail: performedByEmail
    };
    return this.http.post<AuthResponse>(`${this.registerUrl}/approve`, payload);
  }

  rejectUser(targetUserEmail: string) {
    const performedByEmail = localStorage.getItem('userEmail');
    const payload = {
      targetUserEmail: targetUserEmail,
      performedByEmail: performedByEmail
    };
    return this.http.post<AuthResponse>(`${this.registerUrl}/reject`, payload);
  }

  getUserProfile(userEmail: string): Observable<any> {
      return this.http.get<AuthResponse>(`${this.userUrl}/getUserById/${userEmail}`).pipe(
        tap(response => {
          console.log('Response from getUserProfile:', response);
        }),
        catchError(error => {
          console.error('Error fetching user profile:', error);
          if (error.error instanceof ErrorEvent) {
            // Client-side or network error
            console.error('Client-side error:', error.error.message);
        } else {
          // The backend returned an unsuccessful response code
          console.error(`Backend returned code ${error.status}, body was: `, error.error);
        }
        return throwError(() => error);
      })
    );

  }
  requestRecommendation(userEmail: string, file: File): Observable<AuthResponse> {
    debugger;
    const formData: FormData = new FormData();
    formData.append('file', file);

    return this.http.post<AuthResponse>(`${this.baseUrl}/request/${userEmail}`, formData);
  }


  appendRecommendation(id: number, recommendationNotes: string): Observable<AuthResponse> {
    const userEmail = localStorage.getItem('userEmail');
    const payload = {
      recommendationNotes: recommendationNotes,
      userEmail: userEmail
    };
    return this.http.post<AuthResponse>(`${this.baseUrl}/append/${id}`, payload);
  }



  // Download the completed PDF
  downloadPDF(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/download/${id}`, { responseType: 'blob' });
  }

  getReportsForStaff(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.baseUrl}/staff/reports`);
  }

  getReportsForUser(userEmail : string): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.baseUrl}/user/reports/${userEmail}`);
  }

  getAllUsers(page: number = 0, size: number = 10): Observable<any> {
    debugger;
    return this.http.get<any>(`${this.userUrl}/getAllUsers?page=${page}&size=${size}`);
  }

  getAllStaff(page: number = 0, size: number = 10): Observable<any> {
    debugger;
    return this.http.get<any>(`${this.userUrl}/getAllStaff?page=${page}&size=${size}`);
  }

  deleteUserByEmail(email: string, performedByEmail: string): Observable<any> {
    return this.http.delete<any>(`${this.userUrl}/deleteUser/${email}`, {
      params: {
        performedByEmail: performedByEmail
      }
    });
  }



  editUser(formData: FormData): Observable<any> {
    debugger;
    return this.http.post<AuthResponse>(`${this.userUrl}/editUser`, formData);
  }
  changeUserRole(roleChangeRequest: { userEmail: string; newRole: string }): Observable<any> {
    debugger;
    return this.http.post<AuthResponse>(`${this.userUrl}/changeUserRole`, roleChangeRequest);
  }

  getLatestActivities(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.activityUrl}/latest`);
  }

  getActivitiesByEmail(email: string): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.activityUrl}/byEmail/${email}`);
  }

  getAllActivities(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.activityUrl}/all`);
  }
  storeReportWithoutRecommendation(userEmail: string, file: File): Observable<AuthResponse> {
    const formData = new FormData();
    formData.append('pdfFile', file);

    return this.http.post<AuthResponse>(
      `${this.baseUrl}/storeWithoutRecommendation/${userEmail}`,
      formData
    );
  }
}



