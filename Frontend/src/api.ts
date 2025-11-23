// api.ts
import axios, { AxiosRequestConfig } from "axios";

const BASE_URL = "http://192.168.43.201:8080/api/v1";

/* ---------------- UTILITIES ---------------- */

function getToken() {
  return localStorage.getItem("token");
}

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Auto-attach JWT
api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Generic request wrapper
async function request<T>(config: AxiosRequestConfig): Promise<T> {
  try {
    const res = await api.request<T>(config);
    return res.data;
  } catch (err: any) {
    const message =
      err?.response?.data?.message ||
      err?.response?.data ||
      err?.message ||
      "Unknown error";

    throw new Error(message);
  }
}

/* ---------------- AUTH ---------------- */

export interface RegisterInput {
  email: string;
  password: string;
  name: string;
  role: "STUDENT" | "EMPLOYER" | string;
}

export interface RegisterResponse {
  jwt: string;
  userId: number;
  role: string;
  message: string;
}

export async function registerUser(data: RegisterInput): Promise<RegisterResponse> {
  const res = await request<RegisterResponse>({
    url: "/auth/register",
    method: "POST",
    data, // sends all fields, including role
  });

  localStorage.setItem("token", res.jwt);

  return res;
}

/* ---------------- USER ---------------- */

export interface UserMeResponse {
  id: number;
  name: string;
  email: string;
  oneLiner: string | null;
  role: string;
  verificationStatus: string;
  createdAt: string;
  universityName: string | null;
  skills: string[];
  companyContact: string | null;
}

export function getMyProfile() {
  return request<UserMeResponse>({
    url: "/users/me",
    method: "GET",
  });
}

/* ---------------- GIGS ---------------- */

export interface GigInput {
  title: string;
  description: string;
  paymentType: "FIXED" | "HOURLY";
  payRate: number;
  skillsRequired: string[];
}

export interface GigResponse {
  id: number;
  employerId: number;
  title: string;
  description: string;
  paymentType: string;
  payRate: number;
  status: string;
  skillsRequired: string[];
  postedDate: string;
  employerCompanyName: string;
  durationEstimateHours: number | null;
}

/* POST /gigs */
export function createGig(data: GigInput) {
  return request<GigResponse>({
    url: "/gigs",
    method: "POST",
    data,
  });
}

/* GET /gigs */
export function getGigs() {
  return request<GigResponse[]>({
    url: "/gigs",
    method: "GET",
  });
}

/* GET /gigs/my */
export function getMyGig() {
  return request<GigResponse>({
    url: "/gigs/my",
    method: "GET",
  });
}
