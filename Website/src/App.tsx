import React, { useEffect, useRef } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import {
  GraduationCap,
  TrendingUp,
  Briefcase,
  MapPin,
  Clock,
  Award,
  Users,
  ChevronRight,
  Building2,
  School,
  ArrowRight,
  User,
  DollarSign,
} from 'lucide-react';
import logo from "./static/Logo.png";
import { BackedBySection } from './Carousel';

function useIntersectionObserver(ref, options = {}) {
  useEffect(() => {
    if (!ref.current) return;

    const observer = new IntersectionObserver(([entry]) => {
      if (entry.isIntersecting) {
        ref.current.style.animationDelay = '0.2s';
        ref.current.style.animationPlayState = 'running';
      }
    }, { threshold: 0.2, ...options });

    observer.observe(ref.current);
    return () => observer.disconnect();
  }, [ref, options]);
}

// Home Page Component
const Home: React.FC = () => {
  const companies = [
    {
      name: "Tech Innovators Inc.",
      description: "Leading in AI and Machine Learning.",
      location: "San Francisco, CA",
    },
    {
      name: "Green Energy Solutions",
      description: "Renewable energy and sustainability experts.",
      location: "Austin, TX",
    },
    {
      name: "Fintech Revolution",
      description: "Transforming finance with technology.",
      location: "New York, NY",
    },
    {
      name: "HealthTech Co.",
      description: "Innovating in the healthcare space.",
      location: "Boston, MA",
    },
  ];

  return (
    <div className="bg-black text-white min-h-screen">
      {/* Header */}
      <header className="flex items-center justify-between p-4 bg-black/90 border-b border-white/10">
        <div className="flex items-center gap-3">
          <img src={logo} alt="GigTern Logo" className="w-12 h-12 object-contain" />
          <h1 className="text-2xl font-bold">GigTern</h1>
        </div>
        <div>
          <Link
            to="/"
            className="px-4 py-2 rounded-full bg-violet-500 text-white hover:bg-violet-400 transition-colors"
          >
            Log Out
          </Link>
        </div>
      </header>

      <main className="p-8">
        {/* Companies Section */}
        <section className="mb-12">
          <h2 className="text-3xl font-bold mb-4">Companies Looking to Hire</h2>
          <div className="grid md:grid-cols-3 gap-6">
            {companies.map((company, index) => (
              <div key={index} className="p-4 border border-white/10 rounded-lg bg-white/5">
                <h3 className="text-xl font-bold mb-2">{company.name}</h3>
                <p className="text-gray-400 mb-1">{company.description}</p>
                <p className="text-gray-500 text-sm">{company.location}</p>
              </div>
            ))}
          </div>
        </section>

        {/* Account Section */}
        <section>
          <h2 className="text-3xl font-bold mb-4">Your Account</h2>
          <div className="p-6 border border-white/10 rounded-lg bg-white/5">
            <div className="flex items-center gap-4">
              <div className="w-16 h-16 bg-violet-400 rounded-full flex items-center justify-center">
                <User className="w-8 h-8 text-black" />
              </div>
              <div>
                <h3 className="text-xl font-bold">John Doe</h3>
                <p className="text-gray-400">Recruiter</p>
              </div>
            </div>
            <div className="mt-4">
              <p className="text-gray-400">Email: john.doe@example.com</p>
              <p className="text-gray-400">Member since: January 2023</p>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

function LandingPage() {
  const featuresRef = useRef(null);
  const achievementsRef = useRef(null);

  useIntersectionObserver(featuresRef);
  useIntersectionObserver(achievementsRef);

  return (
    <div className="bg-black text-white min-h-screen">
      {/* Header */}
      <nav className="fixed top-6 left-4 right-4 md:left-20 md:right-20 z-50 bg-white/5 backdrop-blur-md border border-white/10 rounded-xl shadow-lg">
        <div className="max-w-7xl mx-auto px-4 md:px-8">
          <div className="flex items-center justify-between h-20">
            <Link to="/" className="flex items-center gap-3">
              <div className="w-24 h-24 bg-transparent rounded-full flex items-center justify-center">
                <img
                  src={logo}
                  alt="GigTern Logo"
                  className="w-full h-full object-contain"
                />
              </div>
              <span className="text-2xl font-bold text-white">GigTern</span>
            </Link>
            <div className="flex items-center gap-6">
              <Link
                to="/home"
                className="px-4 md:px-6 py-2 rounded-full text-sm font-semibold bg-gradient-to-r from-royalViolet to-royalVioletHover text-white hover:from-royalVioletHover hover:to-royalViolet transition-colors"
              >
                Get Started
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <header className="relative py-32 px-4 md:px-8 overflow-hidden mt-20">
        <div className="absolute inset-0 bg-[url('https://images.unsplash.com/photo-1557426272-fc759fdf7a8d?auto=format&fit=crop&q=80')] bg-cover bg-center opacity-5 grayscale"></div>
        <div className="absolute inset-0 bg-gradient-to-b from-black via-transparent to-black"></div>
        <div className="max-w-7xl mx-auto relative">
          <div className="text-center animate-slide-up">
            <h1 className="text-6xl md:text-7xl font-bold mb-8 leading-tight">
              <span className="text-white">Unlock Your Potential</span>
              <br />
              <span className="text-4xl md:text-5xl text-violet-400">
                with Local Opportunities
              </span>
            </h1>
            <p className="text-gray-400 text-xl md:text-2xl mb-12 max-w-3xl mx-auto leading-relaxed">
              Connect with flexible, short-term tasks in your community.
              Build experience, earn income, and make an impact.
            </p>
            <div className="flex justify-center">
              <Link
                to="/home"
                className="bg-gradient-to-r from-royalViolet to-royalVioletHover text-white px-8 py-4 rounded-full text-lg font-semibold inline-flex items-center gap-2 group hover:from-royalVioletHover hover:to-royalViolet transition-colors"
              >
                Get Started
                <ArrowRight className="w-5 h-5 transition-transform group-hover:translate-x-1" />
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* Numbers Section */}
      <ValidationStats />

      {/* Features Section */}
      <section
        ref={featuresRef}
        className="py-32 px-4 md:px-8 bg-white/5"
        style={{ animationPlayState: 'paused' }}
      >
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-20 animate-slide-up">
            <h2 className="text-5xl md:text-6xl font-extrabold mb-6">
              <span className="text-violet-400">What We Offer</span>
            </h2>
            <p className="text-gray-400 text-xl max-w-2xl mx-auto">
              Discover opportunities that fit your schedule and help you grow
            </p>
          </div>
          <div className="grid md:grid-cols-3 gap-8">
            {[
              {
                icon: <Clock />,
                title: 'Flexible Engagements',
                description:
                  'Short-term gigs (1 Hour - 1 Month) that perfectly align with your academic schedule and personal commitments.',
              },
              {
                icon: <MapPin />,
                title: 'Local Impact',
                description:
                  'Find opportunities in your immediate hyperlocal community, earn while maximizing your local influence.',
              },
              {
                icon: <Briefcase />,
                title: 'Real Experience',
                description:
                  'Gain hands-on experience through meaningful tasks that contribute to businesses and improve the economy.',
              },
            ].map((feature, index) => (
              <FeatureCard
                key={index}
                {...feature}
                style={{ animationDelay: `${index * 0.2}s` }}
              />
            ))}
          </div>
        </div>
      </section>

      {/* Achievements Section */}
      <section
        ref={achievementsRef}
        className="py-32 px-4 md:px-8"
        style={{ animationPlayState: 'paused' }}
      >
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-20 animate-slide-up">
            <h2 className="text-5xl md:text-6xl font-extrabold mb-6">
              <span className="text-violet-400">Our Success Story</span>
            </h2>
            <p className="text-gray-400 text-xl max-w-2xl mx-auto">
              Backed by industry leaders and recognized for innovation
            </p>
          </div>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8 justify-items-center">
            {[
              {
                icon: <Users />,
                title: 'Expert Mentorship',
                description:
                  "Strategic guidance from Naukri.com's Ex VP, ensuring cutting-edge technological implementation.",
              },
              {
                icon: <School />,
                title: 'Academic Alliance',
                description:
                  'Strong partnerships 35+ premier communities including E-cells and Startups.',
              },
              {
                icon: <GraduationCap />,
                title: 'Elite Network',
                description:
                  "Partnering with India's top student student communities to deliver exceptional talent.",
              },
              {
                icon: <Award />,
                title: 'National Recognition',
                description:
                  'Winners of major startup competitions at National level.',
              },
              {
                icon: <Building2 />,
                title: 'Strong Backing',
                description:
                  '$4,000 secured in funding ready to help us scale.',
              },
            ].map((achievement, index) => (
              <AchievementCard
                key={index}
                {...achievement}
                style={{ animationDelay: `${index * 0.2}s` }}
              />
            ))}
          </div>
        </div>
      </section>
      
      {/* Backedby Section */}
      <BackedBySection />

      {/* CTA Section */}
      <section className="py-32 px-4 md:px-8 bg-white/5 relative overflow-hidden">
        <div className="absolute inset-0 bg-[url('https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&q=80')] bg-cover bg-center opacity-5 grayscale"></div>
        <div className="max-w-4xl mx-auto relative">
          <div className="text-center animate-fade-in">
            <h2 className="text-4xl md:text-5xl font-bold mb-8 text-white">
              Join the Revolution
            </h2>
            <p className="text-gray-400 text-xl mb-12 leading-relaxed">
              Be part of a community that's redefining how students gain experience and businesses find talent.
            </p>
            <Link
              to="/home"
              className="bg-gradient-to-r from-royalViolet to-royalVioletHover text-white px-12 py-6 rounded-full text-xl font-semibold inline-flex items-center gap-3 group hover:from-royalVioletHover hover:to-royalViolet transition-colors"
            >
              Start Your Journey
              <ChevronRight className="w-6 h-6 transition-transform group-hover:translate-x-1" />
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}

function FeatureCard({ icon, title, description, style }) {
  return (
    <div
      className="p-8 rounded-2xl bg-violet-600/10 border border-white/10 hover:border-violet-400 card-hover transition-colors"
      style={style}
    >
      <div className="w-16 h-16 bg-white rounded-xl flex items-center justify-center mb-6">
        {React.cloneElement(icon, { className: 'w-8 h-8 text-black' })}
      </div>
      <h3 className="text-2xl font-bold mb-4 text-white">{title}</h3>
      <p className="text-gray-400 text-lg leading-relaxed">{description}</p>
    </div>
  );
}


function AchievementCard({ icon, title, description, style }) {
  return (
    <div
      className="p-8 rounded-2xl bg-violet-600/10 border border-white/10 hover:border-violet-500 card-hover"
      style={style}
    >
      <div className="w-16 h-16 bg-white rounded-xl flex items-center justify-center mb-6">
        {React.cloneElement(icon, { className: 'w-8 h-8 text-black' })}
      </div>
      <h3 className="text-2xl font-bold mb-4 text-white">{title}</h3>
      <p className="text-gray-400 text-lg leading-relaxed">{description}</p>
    </div>
  );
}

const App: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/home" element={<Home />} />
    </Routes>
  );
};

const ValidationStats: React.FC = () => {
  const stats = [
    { icon: <DollarSign className="w-12 h-12 text-green-400" />, value: "$4000+", label: "Raised" },
    { icon: <Briefcase className="w-12 h-12 text-purple-400" />, value: "100+", label: "Individuals + Startups for MVP Validation" },
    { icon: <TrendingUp className="w-12 h-12 text-yellow-400" />, value: "10+", label: "POC Validations" },
    { icon: <DollarSign className="w-12 h-12 text-green-400" />, value: "₹40,000", label: "Generated During Validation" },
  ];
  return (
    <section className="py-16 bg-black text-white">
      <div className="max-w-5xl mx-auto px-4">
        <h2 className="text-center text-4xl sm:text-3xl font-bold text-violet-400 mb-12">
          Validation Milestones
        </h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
          {stats.map((stat, index) => (
            <div
              key={index}
              className="flex flex-col items-center bg-violet-300/10 p-6 rounded-xl border border-white/10 hover:border-violet-400 transition-colors"
            >
              <div className="mb-4">{stat.icon}</div>
              <h3 className="text-3xl font-semibold mb-2">{stat.value}</h3>
              <p className="text-lg text-gray-400 text-center">{stat.label}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default App;
